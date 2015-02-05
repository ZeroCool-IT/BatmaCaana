/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.List;

import it.zerocool.batmacaana.database.FavoriteDBHelper;
import it.zerocool.batmacaana.database.FavoriteDBMngr;
import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.Place;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
    private RecyclerView rvResults;
    private ProgressBarCircularIndeterminate progressBar;
    private Context context;
    private RetrieveFavoriteTask task;
    private TextView noResult;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i("ZEROLOG", "OnResume called");
        getData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_favorite, container, false);
        context = getActivity();
        rvResults = (RecyclerView) layout.findViewById(R.id.favorite_recycler_view);
        noResult = (TextView) layout.findViewById(R.id.no_result_tv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResults.setLayoutManager(layoutManager);
        rvResults.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        progressBar = (ProgressBarCircularIndeterminate) layout.findViewById(R.id.favorite_progressBar);
        getData();

        return layout;
    }

    private void getData() {
        task = new RetrieveFavoriteTask();
        task.execute();
    }

    private class RetrieveFavoriteTask extends AsyncTask<Void, Void, List<Place>> {
        private FavoriteDBHelper openHelper;
        private SQLiteDatabase db;

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            rvResults.setVisibility(View.GONE);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param favoriteList The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<Place> favoriteList) {
            progressBar.setVisibility(View.GONE);
            rvResults.setVisibility(View.VISIBLE);


            if (favoriteList != null && !favoriteList.isEmpty()) {
                FavoriteAdapter adapter = new FavoriteAdapter(context, favoriteList, getFragmentManager());
                rvResults.setAdapter(adapter);
            } else {
                String title, message;
                Bundle args = new Bundle();
                WarningDialog dialog = new WarningDialog();
                args.putBoolean(WarningDialog.KILL, false);
                if (favoriteList != null && favoriteList.isEmpty()) {
                    noResult.setVisibility(View.VISIBLE);
                    rvResults.setVisibility(View.GONE);
                    rvResults.invalidate();
                } else {
                    title = getResources().getString(R.string.dialog_title_uhoh);
                    message = getResources().getString(R.string.dialog_message_db_error);
                    Log.e("ZCLOG TASK ERROR", "Failed to get favorite");
                    args.putString(WarningDialog.TITLE, title);
                    args.putString(WarningDialog.MESSAGE, message);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), "Error retrieving data");
                }
            }
            openHelper.close();
        }

        /**
         * <p>Applications should preferably override {@link #onCancelled(Object)}.
         * This method is invoked by the default implementation of
         * {@link #onCancelled(Object)}.</p>
         * <p/>
         * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
         * {@link #doInBackground(Object[])} has finished.</p>
         *
         * @see #onCancelled(Object)
         * @see #cancel(boolean)
         * @see #isCancelled()
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            openHelper.close();
            Log.i("ZCLOG", "onCancelled() called");
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<Place> doInBackground(Void... params) {
            openHelper = FavoriteDBHelper.getInstance(getActivity());
            db = openHelper.getWritabelDB();
            if (isCancelled())
                return null;
            List<Place> res = FavoriteDBMngr.favoriteList(db);

            return res;
        }
    }

}

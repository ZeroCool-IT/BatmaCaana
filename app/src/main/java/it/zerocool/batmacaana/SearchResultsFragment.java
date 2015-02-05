/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.SearchResult;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


public class SearchResultsFragment extends Fragment {


    private RecyclerView rvResults;
    private ProgressBarCircularIndeterminate progressBar;
    private Toolbar toolbar;
    private String query;
    private Context context;
    private SearchTask task;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search_results, container, false);
        toolbar = (Toolbar) layout.findViewById(R.id.appbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        context = getActivity();
        rvResults = (RecyclerView) layout.findViewById(R.id.search_results_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResults.setLayoutManager(layoutManager);
        rvResults.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        progressBar = (ProgressBarCircularIndeterminate) layout.findViewById(R.id.search_result_progressbar);
        query = getArguments().getString(Constraints.QUERY);
        getData(query);

        return layout;
    }

    private void getData(String query) {
        ((ActionBarActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getResources()
                        .getString(R.string.results) + query);

        String uri = Constraints.URI_SEARCH1
                + Constraints.USER_ID
                + Constraints.URI_SEARCH2
                + Uri.encode(query);
        if (RequestUtilities.isOnline(getActivity())) {
            task = new SearchTask();
            task.execute(uri);
        } else {
            String message = getResources().getString(
                    R.string.dialog_message_no_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            WarningDialog dialog = new WarningDialog();
            Bundle arguments = new Bundle();
            arguments.putString(WarningDialog.TITLE, title);
            arguments.putString(WarningDialog.MESSAGE, message);
            arguments.putBoolean(WarningDialog.KILL, true);
            dialog.setArguments(arguments);
            dialog.show(getFragmentManager(), "No Connection warning");
            Log.i("TASK ERROR", "No connection");
        }
    }

    private class SearchTask extends AsyncTask<String, Void, List<SearchResult>> {

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
         * @param searchResults The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<SearchResult> searchResults) {
            progressBar.setVisibility(View.GONE);
            rvResults.setVisibility(View.VISIBLE);


            if (searchResults != null && !searchResults.isEmpty()) {
                SearchAdapter adapter = new SearchAdapter(context, searchResults, getFragmentManager());
                rvResults.setAdapter(adapter);
            } else {
                String title, message;
                Bundle args = new Bundle();
                WarningDialog dialog = new WarningDialog();
                boolean kill = true;
                args.putBoolean(WarningDialog.KILL, kill);
                if (searchResults != null && searchResults.isEmpty()) {
                    title = getResources().getString(R.string.no_results);
                    message = getResources().getString(R.string.no_search_results) + query;
                    Log.i("ZCLOG", "No results!");
                } else {
                    title = getResources().getString(R.string.dialog_title_uhoh);
                    message = getResources().getString(R.string.dialog_message_error);
                    Log.e("ZCLOG TASK ERROR", "Failed to get results");

                }
                args.putString(WarningDialog.TITLE, title);
                args.putString(WarningDialog.MESSAGE, message);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "Error retrieving data");
            }
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
        protected List<SearchResult> doInBackground(String... params) {
            String uri = params[0];
            List<SearchResult> res = null;
            if (isCancelled())
                return null;
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                if (isCancelled())
                    return null;
                res = ParsingUtilities.parseSearchResultsFromJSON(json);
                return res;
            } catch (IOException e) {
                Log.e("ZCLOG TASK", e.getMessage());
                e.printStackTrace();
            }
            return res;
        }
    }

}

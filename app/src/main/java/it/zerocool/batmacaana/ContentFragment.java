/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.Cardable;
import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.model.Route;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.PlaceComparator;
import it.zerocool.batmacaana.utilities.RequestUtilities;
import it.zerocool.batmacaana.utilities.RouteComparator;


public class ContentFragment extends Fragment {

    private RecyclerView rvContent;
    private ContentAdapter adapter;
    private RetrieveDataAsyncTask task;
    private List<Cardable> searchResults;
    private View layout;
    private LayoutInflater inflater;
    private ViewGroup container;
    private ProgressBarCircularIndeterminate progressBar;
    private Location currentLocation;


//    private ImageView ivContent;

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        readCurrentLocationFromPreferences();
        layout = inflater.inflate(R.layout.fragment_content, container, false);
        progressBar = (ProgressBarCircularIndeterminate) layout.findViewById(R.id.progressBar);
        rvContent = (RecyclerView) layout.findViewById(R.id.content_recycler_view);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        int id = getArguments().getInt(Constant.FRAG_SECTION_ID);
        searchResults = getData(getActivity(), id);

        return layout;


    }


    private void readCurrentLocationFromPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        String latitude = sharedPreferences.getString(Constant.LATITUDE, "41.604742");
        String longitude = sharedPreferences.getString(Constant.LONGITUDE, "13.081480");
        Location current = new Location("");
        current.setLatitude(Location.convert(latitude));
        current.setLongitude(Location.convert(longitude));
        currentLocation = current;

    }


    private List<Cardable> getData(Context context, int typeID) {
        String uri = null;
        int type = Constant.PLACE;
        switch (typeID) {
            case Constant.TOSEE:
                uri = Constant.URI_TOSEE;
                break;
            case Constant.EVENT:
                uri = Constant.URI_EVENT;
                type = Constant.EVENT;
                break;
            case Constant.EAT:
                uri = Constant.URI_EAT;
                break;
            case Constant.SLEEP:
                uri = Constant.URI_SLEEP;
                break;
            case Constant.SERVICES:
                uri = Constant.URI_SERVICES;
                break;
            case Constant.CITY:
                uri = Constant.URI_CITY;
                type = Constant.CITY;
                break;
            case Constant.NEWS:
                uri = Constant.URI_NEWS;
                type = Constant.NEWS;
                break;
            case Constant.ROUTES:
                uri = Constant.URI_ROUTES;
                type = Constant.ROUTES;
            default:
                break;
        }
        uri += Integer.valueOf(Constant.USER_ID).toString();

        if (RequestUtilities.isOnline(getActivity())) {
            task = new RetrieveDataAsyncTask();
            task.execute(uri, Integer.valueOf(type).toString());
        } else {
            String message = getResources().getString(
                    R.string.dialog_message_no_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            WarningDialog dialog = new WarningDialog();
            Bundle arguments = new Bundle();
            arguments.putString(WarningDialog.TITLE, title);
            arguments.putString(WarningDialog.MESSAGE, message);
            dialog.setArguments(arguments);
            dialog.show(getFragmentManager(), "No Connection warning");
            Bundle fallbackArgs = new Bundle();
            fallbackArgs.putInt(Constant.FALLBACK_TYPE_ARG, Constant.CONNECTION_ERROR);
            fallbackArgs.putInt(Constant.FALLBACK_REFRESH_ARG, getArguments().getInt(Constant.FRAG_SECTION_ID));
            ContentFallbackFragment f = new ContentFallbackFragment();
            f.setArguments(fallbackArgs);
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            Log.i("TASK ERROR", "No connection");
        }
        return searchResults;
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link android.app.Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
        }

    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (task != null) {
            task.cancel(true);
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link android.app.Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (task != null) {
            task.cancel(true);
        }
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
    }

    private class RetrieveDataAsyncTask extends AsyncTask<String, Void, List<Cardable>> {

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
        protected List<Cardable> doInBackground(String... params) {
            String uri = params[0];
            List<Cardable> res = null;
            int type = Integer.parseInt(params[1]);
            if (isCancelled())
                return null;
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                if (isCancelled())
                    return null;
                switch (type) {
                    case Constant.PLACE:
                        Log.i("ZCLOG", "Current position: " + currentLocation.toString());
                        res = ParsingUtilities.parsePlaceFromJSON(json, currentLocation);
                        ArrayList<Place> temp = (ArrayList) res;
                        Collections.sort(temp, new PlaceComparator());
                        res = (List) temp;
                        break;
                    case Constant.NEWS:
                        res = ParsingUtilities.parseNewsFromJSON(json);
                        break;
                    case Constant.EVENT:
                        res = ParsingUtilities.parseEventFromJSON(json);
                        break;
                    case Constant.CITY:
                        res = ParsingUtilities.parseCitiesFromJSON(json);
                        res = ParsingUtilities.parseCitiesFromJSON(json);
                        temp = (ArrayList) res;
                        Collections.sort(temp, new PlaceComparator());
                        res = (List) temp;
                        break;
                    case Constant.ROUTES:
                        res = ParsingUtilities.parseRoutesFromJSON(json);
                        ArrayList<Route> t = (ArrayList) res;
                        Collections.sort(t, new RouteComparator());
                        res = (List) t;

                }
                if (isCancelled())
                    return null;
                return res;

            } catch (IOException e) {
                Log.e("ZCLOG TASK", e.getMessage());
                e.printStackTrace();
            }
            return res;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param cardables The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<Cardable> cardables) {
            progressBar.setVisibility(View.INVISIBLE);
            rvContent.setVisibility(View.VISIBLE);
            Bundle args = new Bundle();
            args.putInt(Constant.FALLBACK_REFRESH_ARG, getArguments().getInt(Constant.FRAG_SECTION_ID));
            ContentFallbackFragment f = new ContentFallbackFragment();
            FragmentManager fm = getFragmentManager();

            if (cardables != null && cardables.isEmpty()) {
                args.putInt(Constant.FALLBACK_TYPE_ARG, Constant.NO_RESULTS);
                f.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.content_frame, f)
                        .commit();
                Log.i("ZCLOG", "No results!");

            } else if (cardables == null) {
                String title = getResources().getString(R.string.dialog_title_uhoh);
                String message = getResources().getString(R.string.dialog_message_error);
                WarningDialog dialog = new WarningDialog();
                Bundle arguments = new Bundle();
                arguments.putString(WarningDialog.TITLE, title);
                arguments.putString(WarningDialog.MESSAGE, message);
                dialog.setArguments(arguments);
                dialog.show(getFragmentManager(), "Error retrieving data");
                args.putInt(Constant.FALLBACK_TYPE_ARG, Constant.CONNECTION_ERROR);
                f.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.content_frame, f)
                        .commit();
                Log.e("ZCLOG TASK ERROR", "Failed to get results");
            } else {
                adapter = new ContentAdapter(getActivity(), cardables);
                rvContent.setAdapter(adapter);
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
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            rvContent.setVisibility(View.INVISIBLE);
        }
    }

}

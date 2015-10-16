/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


public class DetailsActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context = this;
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final String action = intent.getAction();
        boolean fromNotification = intent.getBooleanExtra(Constant.FLAG_FROM_NOTIFICATION, false);

        if (Intent.ACTION_VIEW.equals(action)) {
            final List<String> segments = intent.getData().getPathSegments();
            getData(segments.get(1));
        } else if (fromNotification) {
            String id = intent.getStringExtra(Constant.ID_ARG);
            String type = intent.getStringExtra(Constant.TYPE_ARG);
            getData(id + "&" + type);
            int typeInt = Integer.parseInt(type);
            switch (typeInt) {
                case Constant.TYPE_NEWS:
                    saveToPreferences(Constant.KEY_NEWS_NOTIFICATION_NUMBER, Integer.valueOf(0).toString());
                    break;
                case Constant.TYPE_EVENT:
                    saveToPreferences(Constant.KEY_EVENT_NOTIFICATION_NUMBER, Integer.valueOf(0).toString());
                    break;
            }

        } else {
            Fragment frag = chooseFragment(intent.getIntExtra(Constant.TYPE_ARG, 0));
            Bundle args = new Bundle();
            args.putString(Constant.JSON_ARG, intent.getStringExtra(Constant.JSON_ARG));
            assert frag != null;
            frag.setArguments(args);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, frag)
                        .commit();
            }
        }
    }

    private void saveToPreferences(String key, String number) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.NOTIFICATION_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, number);
        editor.apply();
    }

    @Nullable
    private Fragment chooseFragment(int type) {
        Fragment f = null;
        switch (type) {
            case Constant.TYPE_PLACE:
                f = new PlaceFragment();
                break;
            case Constant.TYPE_TOSEE:
                f = new PlaceFragment();
                break;
            case Constant.TYPE_SLEEP:
                f = new PlaceFragment();
                break;
            case Constant.TYPE_EAT:
                f = new PlaceFragment();
                break;
            case Constant.TYPE_SERVICE:
                f = new PlaceFragment();
                break;
            case Constant.TYPE_CITY:
                f = new CityFragment();
                break;
            case Constant.TYPE_NEWS:
                f = new NewsFragment();
                break;
            case Constant.TYPE_EVENT:
                f = new EventFragment();
                break;
            case Constant.TYPE_ROUTE:
                f = new RouteFragment();
        }
        return f;
    }

    private void getData(String s) {
        StringTokenizer token = new StringTokenizer(s, "&");

        String[] args = new String[2];
        int cont = 0;
        while (token.hasMoreTokens()) {
            args[cont] = token.nextToken();
            cont++;
        }
        String query = Constant.OBJECT_SEARCH1 +
                args[0] +
                Constant.OBJECT_SEARCH2 +
                args[1];
        String lang = Locale.getDefault().getLanguage().toLowerCase();
        query += Constant.URI_LANGUAGE + lang;
        if (RequestUtilities.isOnline(this)) {
            RequestObjectTask task = new RequestObjectTask();
            task.execute(query, args[1]);
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
            dialog.show(getSupportFragmentManager(), "No Connection warning");
            Log.i("TASK ERROR", "No connection");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_details, menu);
//
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }


    private class RequestObjectTask extends AsyncTask<String, Void, String> {

        private int type;
        private ProgressBarCircularIndeterminate bar;

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            bar = (ProgressBarCircularIndeterminate) findViewById(R.id.details_progressbar);
            bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
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
        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String res;
            String uri = params[0];
            type = Integer.parseInt(params[1]);
            try {
//                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.requestJsonString(uri);
                res = ParsingUtilities.parseSingleResult(json);
            } catch (IOException e) {
                Log.e("TASK ERROR", e.getMessage());
                return null;
            }
            return res;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(@Nullable String s) {
            bar.setVisibility(View.GONE);
            if (s != null && !s.equals(Constant.EMPTY_VALUE)) {

                Fragment frag = chooseFragment(type);
                Bundle args = new Bundle();
                args.putString(Constant.JSON_ARG, s);
                assert frag != null;
                frag.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, frag)
                        .commit();
            } else {
                String title, message;
                Bundle args = new Bundle();
                WarningDialog dialog = new WarningDialog();
                title = context.getResources().getString(R.string.dialog_title_uhoh);
                message = context.getResources().getString(R.string.dialog_message_error);
                args.putString(WarningDialog.TITLE, title);
                args.putString(WarningDialog.MESSAGE, message);
                args.putBoolean(WarningDialog.KILL, true);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "Error retrieving data");
            }
        }
    }
}

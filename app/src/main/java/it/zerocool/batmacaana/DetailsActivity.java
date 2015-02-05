/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


public class DetailsActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context = this;
        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            final List<String> segments = intent.getData().getPathSegments();
            getData(segments.get(1));
        } else {
            Fragment frag = chooseFragment(intent.getIntExtra(Constraints.TYPE_ARG, 0));
            Bundle args = new Bundle();
            args.putString(Constraints.JSON_ARG, intent.getStringExtra(Constraints.JSON_ARG));
            frag.setArguments(args);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, frag)
                        .commit();
            }
        }
    }

    private Fragment chooseFragment(int type) {
        Fragment f = null;
        switch (type) {
            case Constraints.TYPE_PLACE:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_TOSEE:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_SLEEP:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_EAT:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_SERVICE:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_SHOP:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_NEWS:
                f = new NewsFragment();
                break;
            case Constraints.TYPE_EVENT:
                f = new EventFragment();
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
        String query = Constraints.OBJECT_SEARCH1 +
                Constraints.USER_ID +
                Constraints.OBJECT_SEARCH2 +
                args[0] +
                Constraints.OBJECT_SEARCH3 +
                args[1];

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_item_share) {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_SEND);
//            String message = getResources().getString(R.string.share_message) +
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private class RequestObjectTask extends AsyncTask<String, Void, String> {

        private int type;

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
        protected String doInBackground(String... params) {
            String res;
            String uri = params[0];
            type = Integer.parseInt(params[1]);
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                res = ParsingUtilities.parseSingleResult(json);
            } catch (IOException e) {
                Log.e("ZCLOG", e.getMessage());
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
        protected void onPostExecute(String s) {
            if (s != null && !s.equals(Constraints.EMPTY_VALUE)) {

                Fragment frag = chooseFragment(type);
                Bundle args = new Bundle();
                args.putString(Constraints.JSON_ARG, s);
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

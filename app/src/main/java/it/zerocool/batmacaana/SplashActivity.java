/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.ApplicationContextProvider;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;

public class SplashActivity extends ActionBarActivity {

    /**
     * Duration of wait *
     */
    private final static int SPLASH_DISPLAY_LENGTH = 3000;
    private final static int CHECK = 0;
    private final static int UPDATE = 1;

    private boolean empty;
    private CustomersUpdate task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        empty = true;
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.splash_container, new PlaceholderFragment())
                    .commit();
        }

/*        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.splash_color_dark));
        }*/

        SharedPreferences sharedPreferences = this.getSharedPreferences(Constant.PREF_FILE_NAME, MODE_PRIVATE);
        boolean splash = sharedPreferences.getBoolean(Constant.SPLASH, true);

        if (splash) {
            checkCustomersDB();
        } else {
            Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }

    private void checkCustomersDB() {
        task = new CustomersUpdate();
        if (!RequestUtilities.isOnline(this)) {
            String message = getResources().getString(
                    R.string.app_needs_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            WarningDialog dialog = new WarningDialog();
            Bundle arguments = new Bundle();
            arguments.putString(WarningDialog.TITLE, title);
            arguments.putString(WarningDialog.MESSAGE, message);
            arguments.putBoolean(WarningDialog.KILL, true);
            dialog.setArguments(arguments);
            dialog.show(getSupportFragmentManager(), "No Connection warning");
        } else {
            task.execute(UPDATE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_splash, container, false);
        }
    }

    public class CustomersUpdate extends AsyncTask<Integer, Void, Boolean> {

        private int op;
        private SQLiteDatabase db;
        private DBHelper openHelper;

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
        protected Boolean doInBackground(Integer... params) {
            //TODO Change flow, what if customers retrieve fails?

            String uri = Constant.URI_CUSTOMERS;
            openHelper = DBHelper.getInstance(ApplicationContextProvider.getContext());
            db = openHelper.getWritabelDB();
            op = params[0];
            boolean result = false;
            switch (op) {
                case UPDATE:
                    ArrayList<City> results;
                    try {
                        String json = RequestUtilities.requestJsonString(uri);
                        results = ParsingUtilities.parseCustomersFromJSON(json);
                        if (results != null && !results.isEmpty()) {
                            DBManager.clearCustomers(db);
                            DBManager.addCustomers(db, results);
                        }

                    } catch (IOException e) {
                        Log.e("ZCLOG TASK ERROR", e.getMessage());
                        return false;
                    }
                    result = true;
                    break;
                case CHECK:
                    result = DBManager.hasCustomers(db);
                    break;
            }
            return result;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param aVoid The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            switch (op) {
                case CHECK:
                    empty = aBoolean;
                    break;
                case UPDATE:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                            Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
            }

        }
    }
}

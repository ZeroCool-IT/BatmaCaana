/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.dialog.InitialCityDialog;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.splash_container, new SplashFragment())
                    .commit();
        }

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
        CustomersUpdate task = new CustomersUpdate();
        if (!RequestUtilities.isOnline(this)) {
            String message = getResources().getString(
                    R.string.app_needs_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkCustomersDB();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            task.execute();
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
    public static class SplashFragment extends Fragment {

        public SplashFragment() {
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_splash, container, false);
        }
    }

    private class CustomersUpdate extends AsyncTask<Void, Void, Boolean> {

        @Nullable
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
        protected Boolean doInBackground(Void... params) {

            String uri = Constant.URI_CUSTOMERS;
            openHelper = DBHelper.getInstance(ApplicationContextProvider.getContext());
            db = openHelper.getWritabelDB();
            ArrayList<City> results;
            try {
                String json = RequestUtilities.requestJsonString(uri);
                results = ParsingUtilities.parseCustomersFromJSON(json);
                if (results != null && !results.isEmpty()) {
                    assert db != null;
                    DBManager.clearCustomers(db);
                    return DBManager.addCustomers(db, results);
                }
            } catch (IOException e) {
                Log.e("TASK ERROR", e.getMessage());
            }
            return false;
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
        protected void onPostExecute(Boolean success) {
            if (success) {
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREF_FILE_NAME, MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean(Constant.FIRST_TIME, true);
                if (!isFirstTime) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                /* Create an Intent that will start the Main-Activity. */
                            Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            SplashActivity.this.startActivity(mainIntent);
                            SplashActivity.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else {
                    new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                /* Create an Intent that will start the Main-Activity. */
                        InitialCityDialog dialog = new InitialCityDialog();
                        dialog.setCancelable(false);
                        dialog.show(getSupportFragmentManager(), "First time dialog");
                    }
                }, SPLASH_DISPLAY_LENGTH);

                }
            } else {
                String message = getResources().getString(
                        R.string.dialog_message_error);
                String title = getResources().getString(
                        R.string.dialog_title_warning);

                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkCustomersDB();
                    }
                });
                builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SplashActivity.this.finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        }
    }
}


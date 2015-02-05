/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import it.zerocool.batmacaana.dialog.LocationWarningDialog;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.RequestUtilities;
import it.zerocool.batmacaana.utilities.SharedPreferencesProvider;


public class HomeActivity extends ActionBarActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String SERVER_URL = "http://www.ilmiositodemo.altervista.org/app/notifiche/register.php";
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    String SENDER_ID = "557298603924";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;
    private Toolbar toolbar;
    private LocationWarningDialog dialog;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private SharedPreferences prefs;

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestLocationServices();


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = getApplicationContext();

        // Check device for Play Services APK.
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
            NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
            SharedPreferences sp = SharedPreferencesProvider.getSharedPreferences(this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(Constraints.KEY_USER_DEFAULT_START_VIEW, 0);
            editor.apply();
        } else {
            Log.i("PLAY SERVICE ERROR", "No valid Google Play Services APK found.");
        }
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(Constraints.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("GCM ERROR", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("GCM ERROR", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @param context is the application context
     * @return Application's version code from {@code SharedPreferences}
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(HomeActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private void registerInBackground() {
        GcmRegistrationAsyncTask task = new GcmRegistrationAsyncTask(this);
        task.execute();
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("GCM", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constraints.PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String regId) {
        String serverUrl = SERVER_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                RequestUtilities.post(serverUrl, params);
//                GCMRegistrar.setRegisteredOnServer(context, true);
                return;
            } catch (IOException e) {
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
                backoff *= 2;
            }
        }
    }


    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null) {
            dialog.dismiss();
        }
        if (checkPlayServices()) {
            if (locationManager != null && locationListener != null) {
                locationManager.removeUpdates(locationListener);
            }
            requestLocationServices();
        }

    }

    /**
     * Dispatch onPause() to fragments.
     * Stops location update
     */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Stops location update
     */
    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Stops location update on activity destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Create the search option menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        //Get the ID for the search bar LinearLayout
        int searchBarId = searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        //Get the search bar Linearlayout
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(searchBarId);
        //Give the Linearlayout a transition animation.
        searchBar.setLayoutTransition(new LayoutTransition());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save current location on Shared Preferences
     * @param location is the location to save
     */
    private void saveLocationToPreferences(Location location) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constraints.PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        editor.putString(Constraints.LATITUDE, Double.valueOf(latitude).toString());
        editor.putString(Constraints.LONGITUDE, Double.valueOf(longitude).toString());
        editor.apply();
    }

    /**
     * Request the location services
     */
    private void requestLocationServices() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        List<String> availableProvider = locationManager.getProviders(criteria, true);
        if (!availableProvider.contains(LocationManager.GPS_PROVIDER) && !availableProvider.contains(LocationManager.NETWORK_PROVIDER)) {
            dialog = new LocationWarningDialog();
            dialog.show(getSupportFragmentManager(), "Location warning");
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                saveLocationToPreferences(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.w("ZCLOG", "The provider " + provider + " is enabled!");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.w("ZCLOG", "The provider " + provider + " is disabled!");
            }
        };
        if (provider != null && locationManager.getAllProviders().contains(provider)) {
            Log.i("ZCLOG", "Using " + provider + " provider");
            locationManager.requestLocationUpdates(provider, Constraints.LOCATION_UPDATE_TIME,
                    Constraints.LOCATION_MIN_DISTANCE_UPDATE, locationListener);
        } else
            Log.e("ZCLOG", "The provider " + provider + " is not available!");
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("PLAY SERVICE ERROR", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

/*    private class PerformRegistrationTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                sendRegistrationIdToBackend(regid);
                storeRegistrationId(context, regid);

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }

            return msg;
        }

        *//**//**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param o The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     *//**//*
        @Override
        protected void onPostExecute(String o) {
            //mDisplay.append(msg + "\n");
        }
    }*/

}

/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.utilities;

/**
 * Utility class providing application context
 * Also initialize Google Analytics Services
 * Created by Marco on 24/01/2015.
 */

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class ApplicationContextProvider extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;


    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-49979137-4"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        sContext = getApplicationContext();

    }

}

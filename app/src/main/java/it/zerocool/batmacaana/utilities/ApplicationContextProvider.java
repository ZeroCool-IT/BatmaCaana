/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.utilities;

/**
 * Created by Marco on 24/01/2015.
 */

import android.app.Application;
import android.content.Context;

public class ApplicationContextProvider extends Application {

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

        sContext = getApplicationContext();

    }

}

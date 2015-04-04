/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Notifications utilities class
 * Created by Marco Battisti on 03/04/2015.
 */
public class NotificationsUtil {

    private NotificationsUtil() {
    }

    public static boolean isNotificationsDisabled() {
        SharedPreferences sp = ApplicationContextProvider.getContext()
                .getSharedPreferences(Constant.NOTIFICATION_PREFS, Context.MODE_PRIVATE);
        long currentTime = System.currentTimeMillis();
        long enablingTime = sp.getLong(Constant.REACTIVATE_TIME, currentTime);
        return sp.getBoolean(Constant.NOTIFICATION_DISABLED, false) || currentTime < enablingTime;
    }

    public static String getEnablingTimeToString() {
        SharedPreferences sp = ApplicationContextProvider.getContext()
                .getSharedPreferences(Constant.NOTIFICATION_PREFS, Context.MODE_PRIVATE);

        long timeInMillis = sp.getLong(Constant.REACTIVATE_TIME, System.currentTimeMillis());
        Calendar time = GregorianCalendar.getInstance();
        time.setTimeInMillis(timeInMillis);
        java.text.DateFormat dateFormat = SimpleDateFormat.getTimeInstance(java.text.DateFormat.SHORT, Locale.getDefault());
        return dateFormat.format(time.getTime());
    }

    public static long getEnablingTime() {
        SharedPreferences sp = ApplicationContextProvider.getContext()
                .getSharedPreferences(Constant.NOTIFICATION_PREFS, Context.MODE_PRIVATE);

        return sp.getLong(Constant.REACTIVATE_TIME, System.currentTimeMillis());
    }

    public static boolean isNotificationOff() {
        SharedPreferences sp = ApplicationContextProvider.getContext()
                .getSharedPreferences(Constant.NOTIFICATION_PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(Constant.NOTIFICATION_DISABLED, false);
    }


}

/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Marco on 29/01/2015.
 */
public class SharedPreferencesProvider {

    /**
     * Save a string into share preferences
     *
     * @param context         is the activity context
     * @param preferenceName  is the name of the preference's field
     * @param preferenceValue is the value to save
     */
    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constraints.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * Read a string from shared preferences
     *
     * @param context         is the activity context
     * @param preferenceName  is the name of the preference's field
     * @param preferenceValue is the default value to return if the preference doesn't exists
     * @return the String in the field preferenceName
     */
    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constraints.PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferenceValue);
    }

    /**
     * Return the shared preferences for the app
     *
     * @param context is the activity context
     * @return the SharedPreferences object
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constraints.PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}

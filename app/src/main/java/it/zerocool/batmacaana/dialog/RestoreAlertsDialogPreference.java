/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import it.zerocool.batmacaana.utilities.ApplicationContextProvider;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Dialog to restore alerts
 * Created by Marco Battisti on 19/02/2015.
 */
public class RestoreAlertsDialogPreference extends DialogPreference {

    public RestoreAlertsDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link android.content.DialogInterface#BUTTON1}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            SharedPreferences sharedPreferences = ApplicationContextProvider.getContext().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constant.EARTH_NOT_NEEDED, false);
            editor.apply();
        }


    }
}

/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import it.zerocool.batmacaana.R;

/**
 * Warn the user if the location services are disabled
 * Created by Marco on 20/01/2015.
 */
public class LocationWarningDialog extends DialogFragment {

    // --Commented out by Inspection (05/03/2015 16:35):public static final String TITLE = "title";


    public LocationWarningDialog() {
    }


    /**
     * Action performed on creation of dialog
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.location_service_warning_title));
        builder.setMessage(getResources().getString(R.string.location_service_warning));
        builder.setPositiveButton(R.string.dialog_button_settings, new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param id  The button that was clicked (e.g.
             *               {@link android.content.DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });
        builder.setNegativeButton(R.string.dialog_button_ignore, new DialogInterface.OnClickListener() {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link android.content.DialogInterface#BUTTON1}) or the position
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

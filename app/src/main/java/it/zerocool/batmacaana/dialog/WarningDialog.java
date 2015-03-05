/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import it.zerocool.batmacaana.R;

/**
 * Generic Warning Dialog
 * Created by Marco on 14/01/2015.
 */
public class WarningDialog extends DialogFragment {

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String KILL = "kill";
    private boolean killActivity;


    public WarningDialog() {
    }


    /**
     * Action performed on creation of dialog
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);
        killActivity = getArguments().getBoolean(KILL);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.dialog_button_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (killActivity) {
                            getActivity().finish();
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}

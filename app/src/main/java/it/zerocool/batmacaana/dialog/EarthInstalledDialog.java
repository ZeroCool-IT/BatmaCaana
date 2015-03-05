/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import it.zerocool.batmacaana.R;

/**
 * Warn the user if Google Earth is not installed
 * Created by Marco on 14/01/2015.
 */
public class EarthInstalledDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String URL = "url";


    public EarthInstalledDialog() {
    }


    /**
     * Action performed on creation of dialog
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        String title = getString(R.string.earth_warning_title);
        String message = getString(R.string.earth_warning_message);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.earth_warning_positive, this);
        builder.setNegativeButton(R.string.earth_warning_negative, this);
        builder.setNeutralButton(R.string.earth_warning_neutral, this);
        // Create the AlertDialog object and return it
        return builder.create();
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
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.earth"));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                this.dismiss();
                ConfirmationDialog wDialog = new ConfirmationDialog();
                Bundle arguments = new Bundle();
                arguments.putString(URL, getArguments().getString(URL));
                wDialog.setArguments(arguments);
                wDialog.show(getFragmentManager(), "Confirmation");
                break;
        }
    }
}


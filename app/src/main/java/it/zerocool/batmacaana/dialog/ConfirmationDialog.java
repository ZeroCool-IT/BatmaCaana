/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Confirmation dialog
 * Created by Marco on 14/01/2015.
 */
public class ConfirmationDialog extends DialogFragment implements DialogInterface.OnClickListener {


    public ConfirmationDialog() {
    }


    /**
     * Action performed on creation of dialog
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getString(R.string.remember_choose);

        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNegativeButton(R.string.no, this);
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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getArguments().getString(EarthInstalledDialog.URL)));
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constant.EARTH_NOT_NEEDED, true);
                editor.apply();
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}


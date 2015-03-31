/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Dialog for notifications enabling
 * Created by Marco Battisti on 31/03/2015.
 */
public class CityNotificationDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private int cityUid;
    private boolean[] checked;
    private SharedPreferences sp;

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * {@link #onCreateView(LayoutInflater, ViewGroup, android.os.Bundle)} does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p/>
     * <p>This method will be called after {@link #onCreate(android.os.Bundle)} and
     * before {@link #onCreateView(LayoutInflater, ViewGroup, android.os.Bundle)}.  The
     * default implementation simply instantiates and returns a {@link android.app.Dialog}
     * class.
     * <p/>
     * <p><em>Note: DialogFragment own the {@link android.app.Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link android.app.Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override {@link #onCancel(DialogInterface)}
     * and {@link #onDismiss(DialogInterface)}.</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        cityUid = getArguments().getInt(Constant.USER_ID_ARG);
        sp = getActivity().getSharedPreferences(Constant.NOTIFICATION_PREFS, Context.MODE_PRIVATE);
        checked = new boolean[2];
        checked[0] = sp.getBoolean(Constant.NOT_NEWS + cityUid, true);
        checked[1] = sp.getBoolean(Constant.NOT_EVENT + cityUid, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.receive_notification);
        builder.setMultiChoiceItems(R.array.notification_dialog_item, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.dialog_button_ok, this)
                .setNegativeButton(R.string.cancel, this);


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
        if (which == DialogInterface.BUTTON_POSITIVE) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Constant.NOT_NEWS + cityUid, checked[0]);
            editor.putBoolean(Constant.NOT_EVENT + cityUid, checked[1]);
            editor.apply();
        }

    }
}

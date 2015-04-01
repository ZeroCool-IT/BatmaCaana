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
 * Alert dialog for disabling notifications
 * Created by Marco Battisti on 01/04/2015.
 */
public class AlertsDisablingDialog extends DialogFragment implements DialogInterface.OnClickListener {

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
     * To find out about these events, override {@link #onCancel(android.content.DialogInterface)}
     * and {@link #onDismiss(android.content.DialogInterface)}.</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences(Constant.NOTIFICATION_PREFS, Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.notifications);
        builder.setSingleChoiceItems(R.array.disabling_notifications_dialog, 0, this);


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

    }
}

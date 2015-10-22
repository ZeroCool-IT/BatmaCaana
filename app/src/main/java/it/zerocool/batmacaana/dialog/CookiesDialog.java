package it.zerocool.batmacaana.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import it.zerocool.batmacaana.CustomersAdapter;
import it.zerocool.batmacaana.CustomersFragment;
import it.zerocool.batmacaana.HomeActivity;
import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Cookies Law compliance
 * Created by Marco Battisti on 04/09/2015.
 */
public class CookiesDialog extends DialogFragment implements DialogInterface.OnClickListener {

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     *  does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p/>
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * before .  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     * <p/>
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.cookie_law_title)
                .setMessage(R.string.cookies_law)
                .setPositiveButton(R.string.accept, this)
                .setNeutralButton(R.string.privacy_policy, this);

        return builder.create();
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link DialogInterface#BUTTON1}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
            boolean isFirstTime = sharedPreferences.getBoolean(Constant.FIRST_TIME, true);
            if (isFirstTime) {
                /*InitialCityDialog initialCityDialog = new InitialCityDialog();
                initialCityDialog.setCancelable(false);
                initialCityDialog.show(getFragmentManager(), "Initial City Dialog");*/
                CustomersFragment fragment = new CustomersFragment();
                FragmentManager fm = getFragmentManager();

                fm.beginTransaction()
                        .replace(R.id.splash_container, fragment, "TEST")
                        .commit();


            } else {
                Intent mainIntent = new Intent(getActivity(), HomeActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                getActivity().finish();
            }
            CookiesDialog.this.dismiss();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constant.COOKIES_FIRST_TIME, false);
            editor.apply();
        }
        else if (which == DialogInterface.BUTTON_NEUTRAL) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.exploracity.it/privacy"));
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else
                Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
        }
    }
}

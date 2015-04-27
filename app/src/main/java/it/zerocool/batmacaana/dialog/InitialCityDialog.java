/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Query the user about the City to show at first start up
 * Created by Marco Battisti on 27/04/2015.
 */
public class InitialCityDialog extends DialogFragment {

    private ArrayList<City> customers;
    private SharedPreferences.Editor editor;


    public InitialCityDialog() {

    }

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
        DBHelper helper = DBHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getWritabelDB();
        assert db != null;
        customers = DBManager.getCustomers(db);
        CharSequence[] customersNameList = getCitiesName(customers);
        SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(customersNameList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                City target = customers.get(which);
                editor.putString(Constant.CITY_NAME, target.getName());
                editor.putString(Constant.CITY_AVATAR, target.getAvatar());
                editor.putInt(Constant.CITY_UID, target.getUserID());
                editor.apply();
            }
        });
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.city_initial_chooser);
//        builder.setMessage(R.string.city_initial_chooser);
        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    editor.putBoolean(Constant.FIRST_TIME, false);
                    editor.apply();
                    InitialCityDialog.this.dismiss();
                    MoreInfoDialog moreInfoDialog = new MoreInfoDialog();
                    moreInfoDialog.show(getFragmentManager(), "More info dialog");
                }
            }
        });

        return builder.create();

    }

    private CharSequence[] getCitiesName(ArrayList<City> customers) {
        CharSequence[] res = new CharSequence[customers.size()];
        int count = 0;
        for (City c : customers) {
            res[count] = c.getName();
            count++;
        }
        return res;
    }


}

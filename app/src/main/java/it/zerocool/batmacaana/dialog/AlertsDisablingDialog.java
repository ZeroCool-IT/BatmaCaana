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
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.zerocool.batmacaana.DrawerAdapter;
import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.listener.DialogReturnListener;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.NotificationsUtil;

/**
 * Alert dialog for disabling notifications
 * Created by Marco Battisti on 01/04/2015.
 */
public class AlertsDisablingDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener {

    private final static long ONE_HOUR = 3600000;
    private final static long TWO_HOURS = 7200000;
    private final static long EIGHT_HOURS = 28800000;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private TextView enablingTimeTV;
    private DrawerAdapter adapter;


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
        editor = sp.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.notifications_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v);

        enablingTimeTV = (TextView) v.findViewById(R.id.enabling_time_tv);

        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.notifications_radio_group);
        SwitchCompat switchCompat = (SwitchCompat) v.findViewById(R.id.sound_switch);

        boolean soundEnabled = sp.getBoolean(Constant.NOTIFICATIONS_SOUND, true);
        switchCompat.setChecked(soundEnabled);


        if (NotificationsUtil.isNotificationsDisabled()) {
            radioGroup.check(sp.getInt(Constant.RB_SELECTED, R.id.rb_enabled));
            if (!NotificationsUtil.isNotificationOff()) {
                int type = sp.getInt(Constant.RB_SELECTED, R.id.rb_enabled);
                switch (type) {
                    case R.id.rb_disable_8am:
                        java.text.DateFormat extDateFormat = SimpleDateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.SHORT);
                        GregorianCalendar time = new GregorianCalendar();
                        time.setTimeInMillis(NotificationsUtil.getEnablingTime());
                        enablingTimeTV.setText(getString(R.string.disabled_until2) + extDateFormat.format(time.getTime()));
                        break;
                    default:
                        enablingTimeTV.setText(getString(R.string.disabled_until) + NotificationsUtil.getEnablingTimeToString());
                }
                enablingTimeTV.setVisibility(View.VISIBLE);

            } else {
                enablingTimeTV.setVisibility(View.GONE);
            }
        } else {
            radioGroup.check(R.id.rb_enabled);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(Constant.NOTIFICATIONS_SOUND, isChecked);
                editor.apply();
            }
        });


        radioGroup.setOnCheckedChangeListener(this);


        builder.setTitle(R.string.notifications);
//        builder.setSingleChoiceItems(R.array.disabling_notifications_dialog, 0, this);
        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.apply();
                DialogReturnListener activity = (DialogReturnListener) getActivity();
                activity.onDialogReturn();
            }
        });


        return builder.create();

    }


    /**
     * <p>Called when the checked radio button has changed. When the
     * selection is cleared, checkedId is -1.</p>
     *
     * @param group     the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String text = getString(R.string.disabled_until);
        long h = System.currentTimeMillis();
        java.text.DateFormat dateFormat = SimpleDateFormat.getTimeInstance(java.text.DateFormat.SHORT, Locale.getDefault());
        GregorianCalendar tvCalendar = new GregorianCalendar();
        switch (checkedId) {
            case R.id.rb_enabled:
                editor.putBoolean(Constant.NOTIFICATION_DISABLED, false);
                editor.putInt(Constant.RB_SELECTED, R.id.rb_enabled);
                enablingTimeTV.setVisibility(View.GONE);
                break;
            case R.id.rb_diasble_1h:
                h += ONE_HOUR;
                editor.putBoolean(Constant.NOTIFICATION_DISABLED, false);
                editor.putInt(Constant.RB_SELECTED, R.id.rb_diasble_1h);
                tvCalendar.setTimeInMillis(h);
                enablingTimeTV.setText(text + dateFormat.format(tvCalendar.getTime()));
                enablingTimeTV.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_diasble_2h:
                h += TWO_HOURS;
                editor.putBoolean(Constant.NOTIFICATION_DISABLED, false);
                editor.putInt(Constant.RB_SELECTED, R.id.rb_diasble_2h);
                tvCalendar.setTimeInMillis(h);
                enablingTimeTV.setText(text + dateFormat.format(tvCalendar.getTime()));
                enablingTimeTV.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_disable_8h:
                editor.putBoolean(Constant.NOTIFICATION_DISABLED, false);
                editor.putInt(Constant.RB_SELECTED, R.id.rb_disable_8h);
                h += EIGHT_HOURS;
                tvCalendar.setTimeInMillis(h);
                enablingTimeTV.setText(text + dateFormat.format(tvCalendar.getTime()));
                enablingTimeTV.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_disable_8am:
                text = getString(R.string.disabled_until2);
                GregorianCalendar toSet = new GregorianCalendar();
                java.text.DateFormat extDateFormat = SimpleDateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.SHORT);
                toSet.setTimeInMillis(h);
                toSet.set(Calendar.HOUR_OF_DAY, 8);
                toSet.set(Calendar.MINUTE, 0);
                if (h < toSet.getTimeInMillis()) {
                    tvCalendar.setTimeInMillis(h);
                    enablingTimeTV.setText(text + extDateFormat.format(toSet.getTime()));
                    enablingTimeTV.setVisibility(View.VISIBLE);
                } else {
                    toSet.add(Calendar.DATE, 1);
                    tvCalendar.setTimeInMillis(h);
                    enablingTimeTV.setText(text + extDateFormat.format(toSet.getTime()));
                    enablingTimeTV.setVisibility(View.VISIBLE);
                }
                h = toSet.getTimeInMillis();
                editor.putBoolean(Constant.NOTIFICATION_DISABLED, false);
                editor.putInt(Constant.RB_SELECTED, R.id.rb_disable_8am);
                break;
            case R.id.rb_off:
                editor.putBoolean(Constant.NOTIFICATION_DISABLED, true);
                editor.putInt(Constant.RB_SELECTED, R.id.rb_off);
                enablingTimeTV.setVisibility(View.GONE);
                break;
        }
        editor.putLong(Constant.REACTIVATE_TIME, h);
        editor.apply();

    }
}

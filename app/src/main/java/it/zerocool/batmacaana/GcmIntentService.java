/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.ApplicationContextProvider;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.NotificationsUtil;

/**
 * Service listening for notification message.
 * Created by Marco Battisti on 04/02/2015.
 */
public class GcmIntentService extends IntentService {

    private static final int NOTIFICATION_ID_NEWS = 1;
    private static final int NOTIFICATION_ID_EVENT = 2;
    private static final String TAG = "GCM INTENT";
    private static final String NEWS_GROUP = "news";
    private static final String EVENT_GROUP = "event";
    private static final String BASE64_FLAG = "*B64*";
    private final SharedPreferences sharedPreferences;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    @Nullable
    private ArrayList<City> customers;

    public GcmIntentService() {
        super("GcmIntentService");
        sharedPreferences = ApplicationContextProvider.getContext().getSharedPreferences(Constant.NOTIFICATION_PREFS, MODE_PRIVATE);
        /*RetrieveCitiesTask task = new RetrieveCitiesTask();
        task.execute();*/
        DBHelper helper = DBHelper.getInstance(ApplicationContextProvider.getContext());
        SQLiteDatabase db = helper.getWritabelDB();
        assert db != null;
        customers = DBManager.getCustomers(db);
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        /*SharedPreferences sp = getSharedPreferences(Constant.PREF_FILE_NAME, MODE_PRIVATE);
        boolean newsEnabled = sp.getBoolean("news_notification_enabled", true);
        boolean eventEnabled = sp.getBoolean("event_notification_enabled", true);*/

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            switch (messageType) {
                case GoogleCloudMessaging.
                        MESSAGE_TYPE_SEND_ERROR:
                    sendNotification("Send error: " + extras.toString());
                    break;
                case GoogleCloudMessaging.
                        MESSAGE_TYPE_DELETED:
                    sendNotification("Deleted messages on server: " +
                            extras.toString());
                    // If it's a regular GCM message, do some work.
                    break;
                case GoogleCloudMessaging.
                        MESSAGE_TYPE_MESSAGE:
                    Map<String, String> extrasMap = parseMessage(extras);
                    int type = Integer.parseInt(extrasMap.get(Constant.TYPE_ARG));
                    int uid = Integer.parseInt(extrasMap.get(Constant.USER_ID_ARG));
                    switch (type) {
                        case Constant.TYPE_NEWS:
                            if (isEnabled(uid, type)) {
                                sendNewsNotification(extrasMap);
                            }
                            break;
                        case Constant.TYPE_EVENT:
                            if (isEnabled(uid, type)) {
                                sendEventNotification(extrasMap);
                            }
                            break;
                        default:
                            sendNotification(extras.toString());
                            break;
                    }
                    Log.i(TAG, "Received: " + extras.toString());
                    break;
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    @NonNull
    private Map<String, String> parseMessage(@NonNull Bundle extras) {
        Map<String, String> result = new HashMap<>();
        String uid = extras.getString(Constant.USER_ID_ARG);
        result.put(Constant.USER_ID_ARG, uid);
        String id = extras.getString(Constant.ID_ARG);
        result.put(Constant.ID_ARG, id);
        String type = extras.getString(Constant.TYPE_ARG);
        result.put(Constant.TYPE_ARG, type);
        String message = extras.getString(Constant.MESSAGE_ARG);
        if (message.startsWith(BASE64_FLAG)) {
            byte[] out;
            try {
                message = message.substring(BASE64_FLAG.length());
                out = Base64.decode(message, Base64.DEFAULT);
            } catch (IllegalArgumentException e) {
                Log.w("BASE64 ERROR", e.getMessage());
                result.put(Constant.MESSAGE_ARG, message);
                return result;
            }
            String decoded = null;
            try {
                decoded = new String(out, "UTF-8");
                Log.i("BASE64", "String successful decoded: " + decoded);
            } catch (UnsupportedEncodingException e) {
                Log.w("BASE64 ERROR", e.getMessage());
            }

            result.put(Constant.MESSAGE_ARG, decoded);
        } else {
            result.put(Constant.MESSAGE_ARG, message);
        }


        return result;
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        Log.i("NOTIFICATION", msg);
    }

    private void sendNewsNotification(@NonNull Map<String, String> map) {
        int number = Integer.parseInt(sharedPreferences.getString(Constant.KEY_NEWS_NOTIFICATION_NUMBER, "0"));
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constant.FLAG_FROM_NOTIFICATION, true);
        intent.putExtra(Constant.TYPE_ARG, map.get(Constant.TYPE_ARG));
        intent.putExtra(Constant.ID_ARG, map.get(Constant.ID_ARG));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent detailsPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        String message = map.get(Constant.MESSAGE_ARG);

        String city = findNameByUid(map.get(Constant.USER_ID_ARG));
        String title;
        if (city != null && !city.isEmpty()) {
            title = getString(R.string.notification_news_title) +
                    city;
        } else {
            title = getString(R.string.generic_news_notification);

        }


        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_newspaper_white_48dp)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setTicker(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setNumber(++number)
                .setGroup(NEWS_GROUP);
        builder.setContentIntent(detailsPendingIntent);

        boolean isSoundEnabled = sharedPreferences.getBoolean(Constant.NOTIFICATIONS_SOUND, true);

        if (isSoundEnabled) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }

        mNotificationManager.notify(NOTIFICATION_ID_NEWS, builder.build());
        saveToPreferences(Constant.KEY_NEWS_NOTIFICATION_NUMBER, Integer.valueOf(number).toString());
    }

    private void sendEventNotification(@NonNull Map<String, String> map) {
        int number = Integer.parseInt(sharedPreferences.getString(Constant.KEY_EVENT_NOTIFICATION_NUMBER, "0"));
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constant.FLAG_FROM_NOTIFICATION, true);
        intent.putExtra(Constant.TYPE_ARG, map.get(Constant.TYPE_ARG));
        intent.putExtra(Constant.ID_ARG, map.get(Constant.ID_ARG));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent detailsPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        String message = map.get(Constant.MESSAGE_ARG);

        String city = findNameByUid(map.get(Constant.USER_ID_ARG));
        String title;
        if (city != null && !city.isEmpty()) {
            title = getString(R.string.notification_event_title) +
                    city;
        } else {
            title = getString(R.string.generic_event_notification);
        }

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_event_white_48dp)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setTicker(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setNumber(++number)
                .setGroup(EVENT_GROUP);
        builder.setContentIntent(detailsPendingIntent);

        boolean isSoundEnabled = sharedPreferences.getBoolean(Constant.NOTIFICATIONS_SOUND, true);
        if (isSoundEnabled) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }
        mNotificationManager.notify(NOTIFICATION_ID_EVENT, builder.build());
        saveToPreferences(Constant.KEY_EVENT_NOTIFICATION_NUMBER, Integer.valueOf(number).toString());
    }

    private void saveToPreferences(String key, String number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, number);
        editor.apply();
    }

    @Nullable
    private String findNameByUid(String uid) {
        if (customers != null) {
            for (City c : customers) {
                if (c.getUserID() == Integer.parseInt(uid))
                    return c.getName();
            }
        }
        return null;
    }

    private boolean isEnabled(int uid, int type) {
        return isCityEnabled(uid, type) && !NotificationsUtil.isNotificationsDisabled();
    }

    private boolean isCityEnabled(int uid, int type) {
        switch (type) {
            case Constant.TYPE_NEWS:
                return sharedPreferences.getBoolean(Constant.NOT_NEWS + uid, true);
            case Constant.TYPE_EVENT:
                return sharedPreferences.getBoolean(Constant.NOT_EVENT + uid, true);
            default:
                return true;
        }
    }

    /*private class RetrieveCitiesTask extends AsyncTask<Void, Void, ArrayList<City>> {

        *//**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
     *//*
        @NonNull
        @Override
        protected ArrayList<City> doInBackground(Void... params) {
            DBHelper helper = DBHelper.getInstance(ApplicationContextProvider.getContext());
            SQLiteDatabase db = helper.getWritabelDB();
            assert db != null;
            return DBManager.getCustomers(db);
        }

        *//**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param cities The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
     *//*
        @Override
        protected void onPostExecute(@Nullable ArrayList<City> cities) {
            if (cities != null) {
                customers = cities;

            }
        }
    }*/

}

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.zerocool.batmacaana.database.DBHelper;
import it.zerocool.batmacaana.database.DBManager;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.ApplicationContextProvider;
import it.zerocool.batmacaana.utilities.Constant;

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
    private final SharedPreferences sharedPreferences;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private ArrayList<City> customers;

    public GcmIntentService() {
        super("GcmIntentService");
        sharedPreferences = ApplicationContextProvider.getContext().getSharedPreferences(Constant.NOTIFICATION_PREFS, MODE_PRIVATE);
        RetrieveCitiesTask task = new RetrieveCitiesTask();
        task.execute();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        SharedPreferences sp = getSharedPreferences(Constant.PREF_FILE_NAME, MODE_PRIVATE);
        boolean newsEnabled = sp.getBoolean("news_notification_enabled", true);
        boolean eventEnabled = sp.getBoolean("event_notification_enabled", true);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Map<String, String> extrasMap = parseMessage(extras);
                int type = Integer.parseInt(extrasMap.get(Constant.TYPE_ARG));
                switch (type) {
                    case Constant.TYPE_NEWS:
                        if (newsEnabled) {
                            sendNewsNotification(extrasMap);
                        }
                        break;
                    case Constant.TYPE_EVENT:
                        if (eventEnabled) {
                            sendEventNotification(extrasMap);
                        }
                        break;
                    default:
                        sendNotification(extras.toString());
                        break;
                }
                // This loop represents the service doing some work.
/*                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());*/
                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private Map<String, String> parseMessage(Bundle extras) {
        Map<String, String> result = new HashMap<>();
        String uid = extras.getString(Constant.USER_ID_ARG);
        result.put(Constant.USER_ID_ARG, uid);
        Log.i("ZCLOG UID NOTIFICATION", uid);
        String id = extras.getString(Constant.ID_ARG);
        result.put(Constant.ID_ARG, id);
        String type = extras.getString(Constant.TYPE_ARG);
        result.put(Constant.TYPE_ARG, type);
        String message = extras.getString(Constant.MESSAGE_ARG);
        result.put(Constant.MESSAGE_ARG, message);
        return result;
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
/*        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_about)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/
        Log.i("NOTIFICATION", msg);
    }

    private void sendNewsNotification(Map<String, String> map) {
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
                    findNameByUid(map.get(Constant.USER_ID_ARG));
        } else {
            title = getString(R.string.generic_news_notification);

        }

        /*String title = getString(R.string.notification_news_title) +
                getString(R.string.city_name);*/


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_newspaper_white_48dp)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setTicker(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setNumber(++number)
                .setGroup(NEWS_GROUP);
        builder.setContentIntent(detailsPendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID_NEWS, builder.build());
        saveToPreferences(Constant.KEY_NEWS_NOTIFICATION_NUMBER, Integer.valueOf(number).toString());
    }

    private void sendEventNotification(Map<String, String> map) {
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
                    findNameByUid(map.get(Constant.USER_ID_ARG));
        } else {
            title = getString(R.string.generic_event_notification);
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_event_white_48dp)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setTicker(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setNumber(++number)
                .setGroup(EVENT_GROUP);
        builder.setContentIntent(detailsPendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID_EVENT, builder.build());
        saveToPreferences(Constant.KEY_EVENT_NOTIFICATION_NUMBER, Integer.valueOf(number).toString());
    }

    private void saveToPreferences(String key, String number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, number);
        editor.apply();
    }

    private String findNameByUid(String uid) {
        for (City c : customers) {
            if (c.getUserID() == Integer.parseInt(uid))
                return c.getName();
        }
        return null;
    }

    private class RetrieveCitiesTask extends AsyncTask<Void, Void, ArrayList<City>> {

        /**
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
         */
        @Override
        protected ArrayList<City> doInBackground(Void... params) {
            DBHelper helper = DBHelper.getInstance(ApplicationContextProvider.getContext());
            SQLiteDatabase db = helper.getWritabelDB();
            return DBManager.getCustomers(db);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param cities The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            if (cities != null) {
                customers = cities;

            }
        }
    }

}

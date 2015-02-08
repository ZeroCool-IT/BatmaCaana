/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.HashMap;
import java.util.Map;

import it.zerocool.batmacaana.utilities.Constraints;

/**
 * Service listening for notification message.
 * Created by Marco Battisti on 04/02/2015.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GCM INTENT";
    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private int numMessages;

    public GcmIntentService() {
        super("GcmIntentService");
        numMessages = 0;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

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
                sendNotification(parseMessage(extras));
                numMessages++;
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private Map<String, String> parseMessage(Bundle extras) {
        Map<String, String> result = new HashMap<>();
        String id = extras.getString(Constraints.ID_ARG);
        result.put(Constraints.ID_ARG, id);
        String type = extras.getString(Constraints.TYPE_ARG);
        result.put(Constraints.TYPE_ARG, type);
        String message = extras.getString(Constraints.MESSAGE_ARG);
        result.put(Constraints.MESSAGE_ARG, message);
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

    private void sendNotification(Map<String, String> map) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constraints.FLAG_FROM_NOTIFICATION, true);
        intent.putExtra(Constraints.TYPE_ARG, map.get(Constraints.TYPE_ARG));
        intent.putExtra(Constraints.ID_ARG, map.get(Constraints.ID_ARG));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent detailsPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                intent, 0);

        String message = map.get(Constraints.MESSAGE_ARG);
        int type = Integer.parseInt(map.get(Constraints.TYPE_ARG));

        String title;
        switch (type) {
            case Constraints.TYPE_NEWS:
                title = getString(R.string.notification_news_title) +
                        getString(R.string.city_name);
                break;
            case Constraints.TYPE_EVENT:
                title = getString(R.string.notification_event_title) +
                        getString(R.string.city_name);
                break;
            default:
                title = getApplicationContext().getResources().getString(R.string.app_name);
        }
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                .setTicker(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setNumber(++numMessages);
        builder.setContentIntent(detailsPendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}

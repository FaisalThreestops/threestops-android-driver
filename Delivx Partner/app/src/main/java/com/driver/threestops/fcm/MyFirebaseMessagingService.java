package com.driver.threestops.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.driver.threestops.RxObservers.RXMqttMessageObserver;
import com.driver.threestops.app.SplashScreen;
import com.driver.Threestops.R;
import com.driver.threestops.app.bookingRequest.BookingPopUp;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AcknowledgeHelper;
import com.driver.threestops.utility.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    JSONObject received;
    private String message,bid;
    private int action=-1;
    JSONObject jsonObject;

    @Inject
    AcknowledgeHelper acknowledgeHelper;
    private String data;

    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                received = new JSONObject(remoteMessage.getData());

                if(received.has("a")){
                    action = received.getInt("a");
                }
                if(received.has("action")){
                    action = received.getInt("action");
                }
//                if(received.has("msg")){
//                    message = received.getString("msg");
//                }

                if(received.has("title")){
                    message = received.getString("title");
                }

                if(received.has("message")){
                    message = received.getString("message");
                }
                if(received.has("body")){
                    message = received.getString("body");
                }
                if(received.has("data")){
                    data = received.getString("data");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Message data orderID: " +data+" : "+acknowledgeHelper);
            if(action==11||action==10 ||  action==29|| action==14){
                if(data.contains("bookingData")){
                    try {
                        jsonObject=new JSONObject(data);
                        jsonObject=jsonObject.getJSONObject("bookingData");
                        jsonObject.put("action",""+action);
                        sendNotification(message, action);
                        RXMqttMessageObserver.getInstance().emit(jsonObject);

                        Log.d(TAG, "Message data : " +jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if("506".equals(action))
            {
                    Intent stopIntent = new Intent(MyFirebaseMessagingService.this, LocationUpdateService.class);
                    stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                    startService(stopIntent);

//               if(!Utility.isMyServiceRunning(LocationUpdateService.class,MyFirebaseMessagingService.this)){
                    Intent startIntent = new Intent(MyFirebaseMessagingService.this, LocationUpdateService.class);
                    startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);
//                }

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if(action!=1)
        sendNotification(message,action);
    }


    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, int action) {
        Intent intent;
        if (action == 11) {
            intent = new Intent(MyFirebaseMessagingService.this, BookingPopUp.class);
            intent.putExtra("booking_Data", jsonObject.toString());
        } else {
            intent = new Intent(MyFirebaseMessagingService.this, SplashScreen.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        bigText.setBigContentTitle(this.getString(R.string.app_name));
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(
            Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.cancel(0);

        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = getApplicationContext().getString(R.string.app_name_withoutSpace);
        ;// The id of the channel.
        CharSequence name = getApplicationContext().getString(R.string.app_name);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setContentIntent(pendingIntent1)
            .setChannelId(CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setLargeIcon(bitmap)
            .setStyle(bigText);

        notificationManager.notify(0, notificationBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
    }
    /*private void sendNotification(String messageBody) {

        Intent intent;
        intent = new Intent(MyFirebaseMessagingService.this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        bigText.setBigContentTitle(getString(R.string.app_name));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(messageBody))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setStyle(bigText);





        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());


    }*/


    public static boolean isApplicationSentToBackground(final Context context)
    {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName()))
            {
                return true;
            }
        }
        return false;
    }
}
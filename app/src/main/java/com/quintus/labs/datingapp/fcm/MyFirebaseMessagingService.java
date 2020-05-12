package com.quintus.labs.datingapp.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Context mContext;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mContext = this;
        LogUtils.debug("Notifications Received");
        if (remoteMessage == null)
            return;

        try{
            LogUtils.debug("Notifications Data: " + remoteMessage.getData());
            String message;

            if (remoteMessage.getData() != null) {
                message = remoteMessage.getData().get("message");
                if (message != null) {
                    if (!message.equalsIgnoreCase("fcm")) {
                        JSONObject json = new JSONObject(message);
                        handleDataMessage(json);
                    }

                } else {
                    Intent navgationIntent = MainActivity.createIntent(mContext);
                    if (remoteMessage.getNotification() != null) {
                        handleNotification(navgationIntent, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject jsonObject = json;
            String message = jsonObject.optString(AppConstants.Notification.BODY);
            String title = "Huddle";
            Intent navgationIntent = MainActivity.createIntent(mContext);
            handleNotification(navgationIntent, title, message);
        }
        catch (Exception e){

        }
    }

    private void handleNotification(Intent navigationIntent, String title, String message) {
        navigationIntent.putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntentWithParentStack(navigationIntent);


        stackBuilder.editIntentAt(0).putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "huddle_channel_" + System.currentTimeMillis(); // The id of the channel.
            CharSequence name = "huddle"; // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.ic_launcher))
                    .setContentIntent(resultPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setContentText(message)
                    .build();
            notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);

        } else {

            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.bigText(message);

            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.ic_launcher))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)
                    .setContentText(message)
                    .build();
            notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);

        }
    }
    @Override
    public void onNewToken(String token) {
        try{
            TempStorage.setFCMToken(token);
            LogUtils.networkError(token);
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}

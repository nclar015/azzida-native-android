package com.azzida.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.azzida.R;
import com.azzida.perfrences.AppPrefs;
import com.azzida.ui.activity.LoginActivity;
import com.azzida.ui.activity.MainActivity;
import com.azzida.ui.notification.ChatNotificationActivity;
import com.azzida.ui.notification.JobListerNotificationActivity;
import com.azzida.ui.notification.JobSeekerNotificationActivity;
import com.azzida.ui.notification.MyProfileNotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public final static String BROADCAST_ACTION = "Chat";
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            try {
                String strMsg = remoteMessage.getData().get("message");
                String strTitle = remoteMessage.getData().get("title");
                String strJobID = remoteMessage.getData().get("JobId");
                String strFrmUserID = remoteMessage.getData().get("FromUserId");
                String strToUserID = remoteMessage.getData().get("ToUserId");
                String strNotificationType = remoteMessage.getData().get("NotificationType");
                String strReceiverUserName = remoteMessage.getData().get("ReceiverUserName");
                String strSenderFullName = remoteMessage.getData().get("SenderFullName");
                String strSenderProfilePicture = remoteMessage.getData().get("SenderProfilePicture");

                Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));

                if (AppPrefs.getStringKeyvaluePrefs(this,
                        AppPrefs.KEY_USER_LOGIN_STATUS).
                        equalsIgnoreCase("true")) {

                    assert strNotificationType != null;

                    if (strNotificationType.equalsIgnoreCase("Application Accept")) {

                        Intent ApplicationAcceptintent = new Intent(this, JobSeekerNotificationActivity.class);
                        ApplicationAcceptintent.putExtra("JobId", strJobID);
                        ApplicationAcceptintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent ApplicationAcceptpendingIntent = PendingIntent.getActivity(this, 0, ApplicationAcceptintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, ApplicationAcceptpendingIntent);

                    }else if (strNotificationType.equalsIgnoreCase("Job Cancel Request")) {

                        Intent ApplicationAcceptintent = new Intent(this, JobSeekerNotificationActivity.class);
                        ApplicationAcceptintent.putExtra("JobId", strJobID);
                        ApplicationAcceptintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent ApplicationAcceptpendingIntent = PendingIntent.getActivity(this, 0, ApplicationAcceptintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, ApplicationAcceptpendingIntent);

                    }else if (strNotificationType.equalsIgnoreCase("Application Accepted")) {

                        Intent ApplicationAcceptintent = new Intent(this, JobSeekerNotificationActivity.class);
                        ApplicationAcceptintent.putExtra("JobId", strJobID);
                        ApplicationAcceptintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent ApplicationAcceptpendingIntent = PendingIntent.getActivity(this, 0, ApplicationAcceptintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, ApplicationAcceptpendingIntent);

                    } else if (strNotificationType.equalsIgnoreCase("offer Accept")) {

                        Intent Acceptintent = new Intent(this, JobListerNotificationActivity.class);
                        Acceptintent.putExtra("JobId", strJobID);
                        Acceptintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent AcceptpendingIntent = PendingIntent.getActivity(this, 0, Acceptintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, AcceptpendingIntent);

                    } else if (strNotificationType.equalsIgnoreCase("Job Application")) {

                        Intent Applicationintent = new Intent(this, JobListerNotificationActivity.class);
                        Applicationintent.putExtra("JobId", strJobID);
                        Applicationintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent ApplicationpendingIntent = PendingIntent.getActivity(this, 0, Applicationintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, ApplicationpendingIntent);

                    } else if (strNotificationType.equalsIgnoreCase("Chat")) {

                        Intent i = new Intent(BROADCAST_ACTION);

                        sendBroadcast(i);


                        Intent Chatintent = new Intent(this, ChatNotificationActivity.class);
                        Chatintent.putExtra("UserId", strFrmUserID);
                        Chatintent.putExtra("JobId", strJobID);
                        Chatintent.putExtra("Profile", strSenderProfilePicture);
                        Chatintent.putExtra("NewApi", "false");
                        Chatintent.putExtra("Name", strSenderFullName);
                        Chatintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent ChatpendingIntent = PendingIntent.getActivity(this, 0, Chatintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, ChatpendingIntent);


                    } else if (strNotificationType.equalsIgnoreCase("Activity")) {

                        Intent ApplicationAcceptintent = new Intent(this, JobSeekerNotificationActivity.class);
                        ApplicationAcceptintent.putExtra("JobId", strJobID);
                        ApplicationAcceptintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent ApplicationAcceptpendingIntent = PendingIntent.getActivity(this, 0, ApplicationAcceptintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, ApplicationAcceptpendingIntent);

                    } else if (strNotificationType.equalsIgnoreCase("Cash Available")) {

                        Intent CashOut = new Intent(this, MainActivity.class);
                        CashOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent CashOutPendingIntent = PendingIntent.getActivity(this, 0, CashOut, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, CashOutPendingIntent);

                    } else if (strNotificationType.equalsIgnoreCase("Job Complete")) {


                        Intent JobCompleteintent = new Intent(this, MyProfileNotificationActivity.class);
                        JobCompleteintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent JobCompletependingIntent = PendingIntent.getActivity(this, 0, JobCompleteintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, JobCompletependingIntent);


                    } else if (strNotificationType.equalsIgnoreCase("Job Completed")) {


                        Intent JobCompletedintent = new Intent(this, JobListerNotificationActivity.class);
                        JobCompletedintent.putExtra("JobId", strJobID);
                        JobCompletedintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent JobCompletedpendingIntent = PendingIntent.getActivity(this, 0, JobCompletedintent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, JobCompletedpendingIntent);


                    } else {

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent intentPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        setNotifiction(strTitle, strMsg, intentPendingIntent);


                    }

                } else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    setNotifiction(strTitle, strMsg, pendingIntent);

                }


            } catch (Exception je) {
                je.printStackTrace();
            }
        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.notification_icons)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Azzida", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setShowBadge(true);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
            scheduleJob();

        } else {
            // Handle message within 10 seconds
            handleNow();
        }


    }

    private void setNotifiction(String strTitle, String strMsg, PendingIntent pendingIntent) {

        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.notification_icons)
                .setContentTitle(strTitle)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentText(strMsg).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Azzida", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
        scheduleJob();

    }

/*
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());
            String title, message;

            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = getString(R.string.default_notification_channel_id);

            Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setSound(sounduri)
                            .setSmallIcon(R.mipmap.ic_launcher);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Azzida",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());
            scheduleJob();
        } else {
            // Handle message within 10 seconds
            handleNow();
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }*/
    // [END receive_message]


    // [START on_new_token]

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
        storeRegIdInPref(token);
    }


    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void storeRegIdInPref(String token) {

        AppPrefs.setStringKeyvaluePrefsNew(MyFirebaseMessagingService.this, AppPrefs.KEY_DEVICE_ID, token);

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.notification_icons)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

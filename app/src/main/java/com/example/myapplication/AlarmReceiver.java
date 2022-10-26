package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Allows us to create an intent on the notification to load a page.
        Intent notificationIntent = new Intent(context, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        // Creates and sends notification to be displayed on the Android phone

        // Establishes notification variables
        String CHANNEL_ID = "My notification"; // The id of the channel is a string.


        //TODO: Change parameters to be exercise values. For example, the CHANNEL_ID string to be an exercise name and ID to be the exercise ID.

        // Creates a notification with set attributes that will display on an Android phone.
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // This is required for building a notification
                .setContentTitle("Title for notification")       // ALso required
                .setContentText("Text describing notification")  // Also required
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent); // Change this later

        // Allows us to click a notification and send it. It should have a different ID for multiple notifications.
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build()); // Parameters: ID, NotificationCompat

        // End notification creation and display

    }
}
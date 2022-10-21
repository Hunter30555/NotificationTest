package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    // Establishes initial objects that load from activity_main.xml from layout
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android phones above version 8.0 (which is Version Code 0) require notification channels for sending notifications
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Creates the notification channel to display notifications on more recent phones.
            // Parameters: String ID, String NAME, IMPORTANCE
            NotificationChannel channel = new NotificationChannel("My notification", "My notification name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //TODO: Add channel attributes like description or vibration here.
            /*
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            */

            // Create notification channel
            notificationManager.createNotificationChannel(channel);
        }

        b1 = findViewById(R.id.button);
        b1.setOnClickListener(v -> addNotification());
    }// End onCreate


    private void addNotification() {

        //TODO: Change parameters to be exercise values. For example, the CHANNEL_ID string to be an exercise name and ID to be the exercise ID.

        // Creates a notification with set attributes that will display on an Android phone.
        String CHANNEL_ID = "my_channel_01"; // The id of the channel is a string.
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // This is required for building a notification
                .setContentTitle("Title for notification")       // ALso required
                .setContentText("Text describing notification")  // Also required
                .setOngoing(true)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build()); // Parameters: ID, NotificationCompat

        // Allows us to create an intent on the notification to load a page.
        Intent notificationIntent = new Intent(this, ResultActivity.class);
        PendingIntent pendingIntent;

        // Specifies the pendingIntent parameters based on the phone version. Our emulator version is more recent than the M version code.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.setContentIntent(pendingIntent);

        // Allows us to click a notification and
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());


    }// End addNotification
}
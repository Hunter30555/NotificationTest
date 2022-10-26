package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    // Establishes initial objects that load from activity_main.xml from layout
    Button b1, b2;
    String CHANNEL_ID = "My notification"; // The id of the channel is a string.
    NotificationChannel channel;
    final Calendar myCalendar = Calendar.getInstance ();
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        b1.setOnClickListener(v -> showTimePicker());

        b2 = findViewById(R.id.button2);
        b2.setOnClickListener(v -> cancelAlarm());
    }// End onCreate


    private void createNotificationChannel() {

        // Android phones above version 8.0 (which is Version Code 0) require notification channels for sending notifications
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Creates the notification channel to display notifications on more recent phones.
            // Parameters: String ID, String NAME, IMPORTANCE
            channel = new NotificationChannel(CHANNEL_ID, "My notification name", NotificationManager.IMPORTANCE_DEFAULT);
            //channel.setDescription("This is the channel description");
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
    }// End addNotification


    // Creates the timePicker modal on button click. This displays the way to assign the time.
    private void showTimePicker(){
        //TODO: Change default mode to be text mode instead of visual mode
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Notification Time")
                .build();

        // Displays the timePicker as a modal on top of page by using the fragment manager
        picker.show(getSupportFragmentManager(), CHANNEL_ID);//TODO: might change to channel string

        // When the timePicker is applies to be
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If we add a text view display we should display it here
                if(picker.getHour() > 12) {
                    System.out.println(picker.getHour() + " : " + picker.getMinute() + "PM");
                }else{
                    System.out.println(picker.getHour() + " : " + picker.getMinute() + "AM");
                }

                // Calendar will get the values from the timePicker object
                calendar = calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);      // This program does not need to be second specific
                calendar.set(calendar.MILLISECOND, 0); // Also no need to be this specific so we default to 0

                setAlarm();
            }
        });
    }

    private void setAlarm() {
        // Since we are sending information to AlarmReceiver Class we can also send information like the page and exercise attributes through intents.
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0); // Must match with the PendingIntent that was set for cancelling the alarm

        //TODO: Think about changing this to just be .set depending on if we want this to not repeat or repeat every day

        // AlarmManager.RTC_WAKEUP will wake up the phone when the alarm goes off, millis is the time for the alarm to go off, alarm interval, pendingIntent being sent
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm() {
        // Since we are sending information to AlarmReceiver Class we can also send information like the page and exercise attributes through intents.
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0); // Must match with the PendingIntent that was set for the alarm

        // If the alarmManager does not exist create it
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Notification has been removed", Toast.LENGTH_SHORT).show();
    }
}
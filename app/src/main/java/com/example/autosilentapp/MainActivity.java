package com.example.autosilentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText startTimeEditText, endTimeEditText;
    private Button silentButton;


    private AlarmManager alarmManager;
    private PendingIntent startPendingIntent;
    private PendingIntent endPendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTimeEditText = findViewById(R.id.start_time_edittext);
        endTimeEditText = findViewById(R.id.end_time_edittext);
        silentButton = findViewById(R.id.silent_button);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        silentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = startTimeEditText.getText().toString();
                String endTime = endTimeEditText.getText().toString();

                // Validate input
                if (startTime.isEmpty() || endTime.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter valid start and end times", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert start and end times to Calendar instances
                Calendar startCalendar = getTimeCalendar(startTime);
                Calendar endCalendar = getTimeCalendar(endTime);

                long stMilies=convertToMillis(startTime);
                long endMilies=convertToMillis(endTime);

                // Get the current time
                Calendar currentTime = Calendar.getInstance();

                // Check if the start time is in the past
//                if (startCalendar.before(currentTime)) {
//                    Toast.makeText(MainActivity.this, "Invalid start time. Please enter a future time.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Calculate the duration in milliseconds
                long duration = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
                alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);

                Intent startIntent = new Intent("START_SILENT_MODE");
                PendingIntent startPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, startIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, stMilies, startPendingIntent);

                Intent endIntent = new Intent("END_SILENT_MODE");
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, endIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endMilies, endPendingIntent);

                    // Show a toast message with the scheduled silent mode period
                    String startTimeStr = getTimeString(startCalendar);
                    String endTimeStr = getTimeString(endCalendar);
                    Toast.makeText(MainActivity.this, "Phone will be silent from " + startTimeStr + " to " + endTimeStr, Toast.LENGTH_SHORT).show();
            }
    });
}
    private Calendar getTimeCalendar(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private long convertToMillis(String time) {
        // Convert time to milliseconds
        // Implement your own logic here based on the time format you expect
        // This is just a simple example
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60 + minutes) * 60 * 1000;
    }

    private String getTimeString(Calendar calendar) {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return String.format("%02d:%02d", hours, minutes);
    }
}
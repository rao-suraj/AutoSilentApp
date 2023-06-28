package com.example.autosilentapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.database.SessionDao;
import com.example.autosilentapp.database.SessionDatabase;

import java.util.Calendar;

public class SessionBroadcastReceiver extends BroadcastReceiver {
    Session sec;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "AUTO_SILENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (intent != null && audioManager != null) {
            String action = intent.getAction();
            Bundle bundle=intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
            if (bundle!=null)
                sec =(Session)bundle.getSerializable(context.getString(R.string.arg_alarm_obj));

            if(!sec.isRecurring()) {
                if (action != null) {
                    switch (action) {
                        case "com.example.autosilentapp.START_SILENT_MODE":
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, 0);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,3, 0);
                            Toast.makeText(context, "Silent mode activated", Toast.LENGTH_SHORT).show();
                            showNotification(context, "Auto Silent App", "Silent mode activated");
                            break;
                        case "com.example.autosilentapp.END_SILENT_MODE":
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,7, 0);
                            Toast.makeText(context, "Silent mode deactivated ", Toast.LENGTH_SHORT).show();
                            setStartOff(sec,context);
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(NOTIFICATION_ID);
                            break;
                    }
                } else {
                    Toast.makeText(context, "action", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (isAlarmToday(sec)) {
                    switch (action) {
                        case "com.example.autosilentapp.START_SILENT_MODE":
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, 0);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,3, 0);
                            Toast.makeText(context, "Silent mode activated", Toast.LENGTH_SHORT).show();
                            showNotification(context, "Auto Silent App", "Silent mode activated");
                            break;
                        case "com.example.autosilentapp.END_SILENT_MODE":
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,7, 0);

                            Toast.makeText(context, "Silent mode deactivated", Toast.LENGTH_SHORT).show();
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(NOTIFICATION_ID);
                            break;
                    }
                }
            }
        }
        else {
            Toast.makeText(context,"Intent of audioManager",Toast.LENGTH_SHORT).show();
        }
    }

    private void setStartOff(Session sec, Context context) {
        SessionDatabase db= SessionDatabase.getDatabase(context);
        SessionDao dao=db.sessionDao();
        sec.setStarted(false);
        SessionDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(sec);
        });
    }

    private boolean isAlarmToday(Session session1) {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (session1.isMonday())
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (session1.isTuesday())
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (session1.isWednesday())
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (session1.isThursday())
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (session1.isFriday())
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (session1.isSaturday())
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (session1.isSunday())
                    return true;
                return false;
        }
        return false;
    }
    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle=new Bundle();

        // Create an intent for the dismiss action
        Intent dismissIntent = new Intent(context, SessionBroadcastReceiver.class);
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),sec);
        dismissIntent.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        dismissIntent.setAction("com.example.autosilentapp.END_SILENT_MODE");
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_looks_one_black)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent);

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
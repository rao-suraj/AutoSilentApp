package com.example.autosilentapp.broadcast_receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.autosilentapp.R;
import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.database.SessionDao;
import com.example.autosilentapp.database.SessionDatabase;

import java.util.Calendar;

public class SessionBroadcastReceiver extends BroadcastReceiver {
    Session sec;
    Profile prof;
    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_PROFILE_ID=2;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "AUTO_SILENT";

    final String START_TIMER_STRING="com.example.autosilentapp.START_SILENT_MODE";
    final String END_TIMER_STRING="com.example.autosilentapp.END_SILENT_MODE";
    final String START_PROFILE_STRING="com.example.autosilentapp.START_PROFILE_MODE";
    final String END_PROFILE_STRING="com.example.autosilentapp.END_PROFILE_MODE";
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (intent != null && audioManager != null) {
            String action = intent.getAction();
            if(action ==START_TIMER_STRING || action==END_TIMER_STRING) {
                Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
                if (bundle != null)
                    sec = (Session) bundle.getSerializable(context.getString(R.string.arg_alarm_obj));

                if (!sec.isRecurring()) {
                    if (action != null) {
                        switch (action) {
                            case "com.example.autosilentapp.START_SILENT_MODE":
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                Toast.makeText(context, "Silent mode activated", Toast.LENGTH_SHORT).show();
                                showNotification(context, "Auto Silent App", "Silent mode activated");
                                break;
                            case "com.example.autosilentapp.END_SILENT_MODE":
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                Toast.makeText(context, "Silent mode deactivated ", Toast.LENGTH_SHORT).show();
                                setStartOff(sec, context);
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(NOTIFICATION_ID);
                                break;
                        }
                    } else {
                        Toast.makeText(context, "action", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isAlarmToday(sec)) {
                        switch (action) {
                            case "com.example.autosilentapp.START_SILENT_MODE":
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                Toast.makeText(context, "Silent mode activated", Toast.LENGTH_SHORT).show();
                                showNotification(context, "Auto Silent App", "Silent mode activated");
                                break;
                            case "com.example.autosilentapp.END_SILENT_MODE":
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                Toast.makeText(context, "Silent mode deactivated", Toast.LENGTH_SHORT).show();
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(NOTIFICATION_ID);
                                break;
                        }
                    }
                }
            } else {
                int previousRingVol=7;
                int previousMediaVol=7;
                Toast.makeText(context,"PROFILE",Toast.LENGTH_SHORT).show();
                Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_profile_obj));
                if (bundle != null)
                    prof = (Profile) bundle.getSerializable(context.getString(R.string.arg_profile_obj));
                if (action != null) {
                        switch (action) {
                            case START_PROFILE_STRING :
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                previousRingVol= audioManager.getStreamVolume(AudioManager.STREAM_RING);
                                previousMediaVol=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, prof.getMediaVol(), 0);
                                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,prof.getRingVol(), 0);
                                Toast.makeText(context, "Profile-Silent mode activated", Toast.LENGTH_SHORT).show();
                                showNotificationProfile(context, "Auto Silent App", "Silent Profile mode activated");
                                break;
                            case END_PROFILE_STRING :
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousMediaVol, 0);
                                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,previousRingVol, 0);
                                Toast.makeText(context, "Profile-Silent mode deactivated ", Toast.LENGTH_SHORT).show();
                                setStartOffProfile(sec, context);
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(NOTIFICATION_PROFILE_ID);
                                break;
                        }
                    } else {
                        Toast.makeText(context, "action null", Toast.LENGTH_SHORT).show();
                    }
            }
        }
        else {
            Toast.makeText(context,"Intent of audioManager",Toast.LENGTH_SHORT).show();
        }
    }

    private void setStartOffProfile(Session sec, Context context) {

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
    private void showNotificationProfile(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle=new Bundle();

        // Create an intent for the dismiss action
        Intent dismissIntent = new Intent(context, SessionBroadcastReceiver.class);
        bundle.putSerializable(context.getString(R.string.arg_profile_obj),prof);
        dismissIntent.putExtra(context.getString(R.string.bundle_profile_obj),bundle);
        dismissIntent.setAction(END_PROFILE_STRING);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_looks_one_black)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent);

        // Show the notification
        notificationManager.notify(NOTIFICATION_PROFILE_ID, builder.build());
    }
}
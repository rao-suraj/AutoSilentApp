package com.example.autosilentapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.autosilentapp.database.Session;

import java.util.Calendar;

public class SessionBroadcastReceiver extends BroadcastReceiver {
    Session sec;

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
                            break;
                        case "com.example.autosilentapp.END_SILENT_MODE":
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,7, 0);
                            Toast.makeText(context, "Silent mode deactivated", Toast.LENGTH_SHORT).show();
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
                            break;
                        case "com.example.autosilentapp.END_SILENT_MODE":
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
//                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,7, 0);

                            Toast.makeText(context, "Silent mode deactivated", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        }
        else {
            Toast.makeText(context,"Intent of audioManager",Toast.LENGTH_SHORT).show();
        }
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
}
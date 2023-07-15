package com.example.autosilentapp.database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.autosilentapp.R;
import com.example.autosilentapp.broadcast_receiver.SessionBroadcastReceiver;
import com.example.autosilentapp.util.DayUtil;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "profile_table")
public class Profile implements Serializable {
    @PrimaryKey
    private int profileId;
    private String title;
    private int startHour, startMinute;
    private int endHour, endMinute;
    private boolean started;
    private int mediaVol,ringVol;

    public Profile(){
        //Empty Constructor
    }
    @Ignore
    public Profile(int profileId, String title, int startHour, int startMinute, int endHour, int endMinute, boolean started, int mediaVol, int ringVol) {
        this.profileId = profileId;
        this.title = title;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.started = started;
        this.mediaVol = mediaVol;
        this.ringVol = ringVol;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getMediaVol() {
        return mediaVol;
    }

    public void setMediaVol(int mediaVol) {
        this.mediaVol = mediaVol;
    }

    public int getRingVol() {
        return ringVol;
    }

    public void setRingVol(int ringVol) {
        this.ringVol = ringVol;
    }



    public void schedule(Context context, Profile profile)
    {
        final String START_PROFILE_STRING="com.example.autosilentapp.START_PROFILE_MODE";
        final String END_PROFILE_STRING="com.example.autosilentapp.END_PROFILE_MODE";
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent startIntent =new Intent(context, SessionBroadcastReceiver.class);
        Intent endIntent =new Intent(context, SessionBroadcastReceiver.class);


        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_profile_obj),profile);
        startIntent.putExtra(context.getString(R.string.bundle_profile_obj),bundle);
        endIntent.putExtra(context.getString(R.string.bundle_profile_obj),bundle);


        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(System.currentTimeMillis());
        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
        startCalendar.set(Calendar.MINUTE, startMinute);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(System.currentTimeMillis());
        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        endCalendar.set(Calendar.MINUTE, endMinute);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        if (startCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH) + 1);
            endCalendar.set(Calendar.DAY_OF_MONTH,endCalendar.get(Calendar.DAY_OF_MONTH)+1);
        }

            String toastText = null;
            try {
                toastText = String.format("Profile-Phone will be silent for %s from  %02d:%02d to %02d:%02d", DayUtil.toDay(startCalendar.get(Calendar.DAY_OF_WEEK)), startHour,startMinute,endHour,endMinute);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String pid=String.valueOf(profile.getProfileId());
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            startIntent.setAction(START_PROFILE_STRING);
            PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, profileId + 1005, startIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    startCalendar.getTimeInMillis(),
                    startPendingIntent
            );
            endIntent.setAction(END_PROFILE_STRING);
            PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, profileId - 1005, endIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    endCalendar.getTimeInMillis(),
                    endPendingIntent
            );
        this.started=true;
    }

    public void cancelAlarm(Context context,Profile profile) {
        final String START_PROFILE_STRING="com.example.autosilentapp.START_PROFILE_MODE";
        final String END_PROFILE_STRING="com.example.autosilentapp.END_PROFILE_MODE";
        Bundle bundle=new Bundle();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent startIntent = new Intent(context, SessionBroadcastReceiver.class);
        bundle.putSerializable(context.getString(R.string.arg_profile_obj),profile);
        startIntent.putExtra(context.getString(R.string.bundle_profile_obj),bundle);
        startIntent.setAction(START_PROFILE_STRING);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, profile.getProfileId() + 1005, startIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(startPendingIntent);

        Intent endIntent = new Intent(context, SessionBroadcastReceiver.class);
        bundle.putSerializable(context.getString(R.string.arg_profile_obj),this);
        endIntent.putExtra(context.getString(R.string.bundle_profile_obj),bundle);
        endIntent.setAction(END_PROFILE_STRING);
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, profile.getProfileId() - 1005, endIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(endPendingIntent);

        this.started = false;
        String toastText = String.format("Profile cancelled for %02d:%02d to %02d:%02d", startHour, startMinute,endHour,endMinute);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }
}

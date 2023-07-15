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

import com.example.autosilentapp.broadcast_receiver.SessionBroadcastReceiver;
import com.example.autosilentapp.R;
import com.example.autosilentapp.util.DayUtil;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "session_table")
public class Session implements Serializable {
    @PrimaryKey
    private int sessionId;
    private int startHour, startMinute;
    private int endHour, endMinute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title;

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }


@Ignore
    public Session(int sessionId,int startHour, int startMinute, int endHour, int endMinute, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, String title) {
        this.sessionId=sessionId;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.title = title;
    }

    public Session() {
        // Empty constructor
    }

    public int getSessionId() {
        return sessionId;
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

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void schedule(Context context,Session session)
    {
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent startIntent =new Intent(context, SessionBroadcastReceiver.class);
        Intent endIntent =new Intent(context, SessionBroadcastReceiver.class);


        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),session);
        startIntent.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        endIntent.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);


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

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("Phone will be silent for %s from  %02d:%02d to %02d:%02d", DayUtil.toDay(startCalendar.get(Calendar.DAY_OF_WEEK)), startHour,startMinute,endHour,endMinute);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sid=String.valueOf(session.getSessionId());
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            startIntent.setAction("com.example.autosilentapp.START_SILENT_MODE");
            PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, sessionId + 1005, startIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    startCalendar.getTimeInMillis(),
                    startPendingIntent
            );
            endIntent.setAction("com.example.autosilentapp.END_SILENT_MODE");
            PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, sessionId - 1005, endIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    endCalendar.getTimeInMillis(),
                    endPendingIntent
            );

        }else{
            String toastText = String.format("Recurring Session scheduled for %s from %02d:%02d to %02d:%02d",  getRecurringDaysText(), startHour, startMinute,endHour,endMinute);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            startIntent.setAction("com.example.autosilentapp.START_SILENT_MODE");
            PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, sessionId + 1005, startIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    startCalendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    startPendingIntent);
            endIntent.setAction("com.example.autosilentapp.END_SILENT_MODE");
            PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, sessionId - 1005, endIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating( AlarmManager.RTC_WAKEUP,
                    endCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, endPendingIntent);


        }
        this.started=true;
    }
    public void cancelAlarm(Context context,Session session) {
        Bundle bundle=new Bundle();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent startIntent = new Intent(context, SessionBroadcastReceiver.class);
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),this);
        startIntent.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        startIntent.setAction("com.example.autosilentapp.START_SILENT_MODE");
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, session.getSessionId() + 1005, startIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(startPendingIntent);

        Intent endIntent = new Intent(context, SessionBroadcastReceiver.class);
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),this);
        endIntent.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        endIntent.setAction("com.example.autosilentapp.END_SILENT_MODE");
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, session.getSessionId() - 1005, endIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(endPendingIntent);

        this.started = false;
        String toastText = String.format("Alarm cancelled for %02d:%02d to %02d:%02d", startHour, startMinute,endHour,endMinute);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }
    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += "Mo ";
        }
        if (tuesday) {
            days += "Tu ";
        }
        if (wednesday) {
            days += "We ";
        }
        if (thursday) {
            days += "Th ";
        }
        if (friday) {
            days += "Fr ";
        }
        if (saturday) {
            days += "Sa ";
        }
        if (sunday) {
            days += "Su ";
        }

        return days;
    }

}

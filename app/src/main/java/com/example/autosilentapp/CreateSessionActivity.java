package com.example.autosilentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.autosilentapp.adapter.SessionRecyclerViewAdapter;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.databinding.ActivityCreateSessionBinding;
import com.example.autosilentapp.viewmodel.CreateSessionViewModel;

import java.util.Calendar;
import java.util.Random;

public class CreateSessionActivity extends AppCompatActivity {

    private int sessionId;
    private int startHour, startMinute;
    private int endHour, endMinute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title=null;
    Session sec;

    private ActivityCreateSessionBinding binding;
    CreateSessionViewModel createSessionViewModel;
    SessionRecyclerViewAdapter sessionRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityCreateSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getBundleExtra(this.getString(R.string.bundle_alarm_obj));
        if (bundle != null) {
            sec = (Session) bundle.getSerializable(getString(R.string.arg_alarm_obj));
            String id= String.valueOf(sec.getSessionId());
//            Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(this,"Empty Session",Toast.LENGTH_SHORT).show();
        }
        if(sec!=null)
        {
            updateAlarmInfo(sec);
            startHour = sec.getStartHour();
            startMinute = sec.getStartMinute();
            endHour = sec.getEndHour();
            endMinute = sec.getEndMinute();
        }


        createSessionViewModel= new ViewModelProvider(this).get(CreateSessionViewModel.class);
        binding.startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int stHour;
                int stMin;
                if(sec!=null)
                {
                    stHour=sec.getStartHour();
                    stMin=sec.getStartMinute();
                }
                else {
                    stHour=22;
                    stMin=30;
                }

                // Create and show the Time Picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateSessionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time
                                // hourOfDay and minute represent the selected time values
                                // You can do something with the selected time here
                                // For example, update a TextView with the selected time
                                startHour=hourOfDay;
                                startMinute=minute;
                            }
                        },
                        stHour, // Default hour (e.g., current hour)
                        stMin, // Default minute (e.g., current minute)
                        true // Set to true if you want to use 24-hour format, false for AM/PM format
                );
                timePickerDialog.show();
            }
        });
        binding.endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int enHour;
                int enMin;
                if(sec!=null)
                {
                    enHour=sec.getEndHour();
                    enMin=sec.getEndMinute();
                }
                else {
                    enHour=22;
                    enMin=30;
                }
                // Create and show the Time Picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateSessionActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Handle the selected time
                                // hourOfDay and minute represent the selected time values
                                // You can do something with the selected time here
                                // For example, update a TextView with the selected time
                               endHour=hourOfDay;
                               endMinute=minute;
                            }
                        },
                        enHour, // Default hour (e.g., current hour)
                        enMin, // Default minute (e.g., current minute)
                        true // Set to true if you want to use 24-hour format, false for AM/PM format
                );
                timePickerDialog.show();
            }
        });
        binding.fragmentCreatealarmRecurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.fragmentCreatealarmRecurringOptions.setVisibility(View.VISIBLE);
                } else {
                    binding.fragmentCreatealarmRecurringOptions.setVisibility(View.GONE);
                }
            }
        });
      binding.fragmentCreatealarmScheduleAlarm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
//               sec=new Session(startHour,startMinute,endHour,endMinute,false,binding.checkbox1.isChecked(),
//                      binding.checkBox2.isChecked(),binding.checkBox3.isChecked(),binding.checkBox4.isChecked(),
//                      binding.checkBox5.isChecked(),binding.checkBox6.isChecked(),binding.checkBox7.isChecked(),
//                      binding.checkBox8.isChecked(),null);
//              sec=new Session();
//              sec.setStartHour(21);
//              sec.setEndHour(21);
//              sec.setStartMinute(15);
//              sec.setEndMinute(16);
//              sec.setStarted(false);
//              sec.setRecurring(true);
//              sec.setMonday(false);
//              sec.setTuesday(false);
//              sec.setWednesday(false);
//              sec.setThursday(false);
//              sec.setFriday(false);
//              sec.setSaturday(true);
//              sec.setSunday(false);
//              sec.schedule(CreateSessionActivity.this);
//              createSessionViewModel.insert(sec);
              if(checkTime()) {
                  if (sec == null) {
                      scheduleSession();
                  } else {
                      updateSession();
                  }
              }else{
                  Toast.makeText(CreateSessionActivity.this,"Start time must be less then end time",Toast.LENGTH_SHORT).show();
              }
          }
      });

    }
    private void scheduleSession() {
        int sessionId = new Random().nextInt(1000);
        Session session =new Session();
        session.setSessionId(sessionId);
        session.setStartHour(startHour);
        session.setStartMinute(startMinute);
        session.setEndHour(endHour);
        session.setEndMinute(endMinute);
        session.setStarted(false);
        if(binding.fragmentCreatealarmRecurring.isChecked()){
            session.setRecurring(true);
        } else {
            session.setRecurring(false);
        }
        if(binding.fragmentCreatealarmCheckMon.isChecked()) {
            session.setMonday(true);}else {
            session.setMonday(false); }
        if(binding.fragmentCreatealarmCheckTue.isChecked()){
            session.setTuesday(true);}else {
            session.setTuesday(false);
        }
        if(binding.fragmentCreatealarmCheckWed.isChecked()){
            session.setWednesday(true);}else{
            session.setWednesday(false);}
        if( binding.fragmentCreatealarmCheckThu.isChecked()){
            session.setThursday(true);}else{
            session.setThursday(false);
        }
        if(binding.fragmentCreatealarmCheckFri.isChecked()){
            session.setFriday(true);}else {
            session.setFriday(false);
        }
        if(binding.fragmentCreatealarmCheckSat.isChecked()){
            session.setSaturday(true);}else{
            session.setSaturday(false);
        }
        if(binding.fragmentCreatealarmCheckSun.isChecked()){
            session.setSunday(true);}else{
            session.setSunday(false);
        }
        session.setTitle(title);
        session.schedule(getApplicationContext(),session);
        createSessionViewModel.insert(session);
    }

    private void updateAlarmInfo(Session session){
        binding.fragmentCreatealarmTitle.setText(session.getTitle());

        if(session.isRecurring()){
            binding.fragmentCreatealarmRecurring.setChecked(true);
            binding.fragmentCreatealarmRecurringOptions.setVisibility(View.VISIBLE);
            if(session.isMonday())
                binding.fragmentCreatealarmCheckMon.setChecked(true);
            if(session.isTuesday())
                binding.fragmentCreatealarmCheckTue.setChecked(true);
            if(session.isWednesday())
                binding.fragmentCreatealarmCheckWed.setChecked(true);
            if(session.isThursday())
                binding.fragmentCreatealarmCheckThu.setChecked(true);
            if(session.isFriday())
                binding.fragmentCreatealarmCheckFri.setChecked(true);
            if(session.isSaturday())
                binding.fragmentCreatealarmCheckSat.setChecked(true);
            if(session.isSunday())
                binding.fragmentCreatealarmCheckSun.setChecked(true);
        }
    }
    private void updateSession(){
//        Session updatedAlarm = new Session(
//                startHour,
//                startMinute,
//                endHour,
//                endMinute,
//                sec.isStarted(),
//                binding.fragmentCreatealarmRecurring.isChecked(),
//                binding.fragmentCreatealarmCheckMon.isChecked(),
//                binding.fragmentCreatealarmCheckTue.isChecked(),
//                binding.fragmentCreatealarmCheckWed.isChecked(),
//                binding.fragmentCreatealarmCheckThu.isChecked(),
//                binding.fragmentCreatealarmCheckFri.isChecked(),
//                binding.fragmentCreatealarmCheckSat.isChecked(),
//                binding.fragmentCreatealarmCheckSun.isChecked(),sec.getTitle()
//        );
//        createSessionViewModel.update(updatedAlarm);
//        updatedAlarm.schedule(this);
        Session  updateSession=new Session();
        updateSession.setSessionId(sec.getSessionId());
        updateSession.setStartHour(startHour);
        updateSession.setStartMinute(startMinute);
        updateSession.setEndHour(endHour);
        updateSession.setEndMinute(endMinute);
        updateSession.setStarted(false);
        if(binding.fragmentCreatealarmRecurring.isChecked()){
            updateSession.setRecurring(true);
        } else {
            updateSession.setRecurring(false);
        }
        if(binding.fragmentCreatealarmCheckMon.isChecked()) {
            updateSession.setMonday(true);}else {
            updateSession.setMonday(false); }
        if(binding.fragmentCreatealarmCheckTue.isChecked()){
            updateSession.setTuesday(true);}else {
            updateSession.setTuesday(false);
        }
        if(binding.fragmentCreatealarmCheckWed.isChecked()){
            updateSession.setWednesday(true);}else{
            updateSession.setWednesday(false);}
        if( binding.fragmentCreatealarmCheckThu.isChecked()){
            updateSession.setThursday(true);}else{
            updateSession.setThursday(false);
        }
        if(binding.fragmentCreatealarmCheckFri.isChecked()){
            updateSession.setFriday(true);}else {
            updateSession.setFriday(false);
        }
        if(binding.fragmentCreatealarmCheckSat.isChecked()){
            updateSession.setSaturday(true);}else{
            updateSession.setSaturday(false);
        }
        if(binding.fragmentCreatealarmCheckSun.isChecked()){
            updateSession.setSunday(true);}else{
            updateSession.setSunday(false);
        }
        updateSession.setTitle(title);
        createSessionViewModel.update(updateSession);
    }
    public boolean checkTime() {
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

        if (startCalendar.getTimeInMillis() >= endCalendar.getTimeInMillis()) {
            return false;
        } else {
            return true;
        }
    }
}
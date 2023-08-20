package com.example.autosilentapp.presentation.create_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.autosilentapp.R;
import com.example.autosilentapp.database.local.model.Profile;
import com.example.autosilentapp.databinding.ActivityCreateProfileBinding;

import java.util.Calendar;
import java.util.Random;

public class CreateProfileActivity extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    AudioManager audioManager;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;

    private int ringVol,mediaVol;
    String title=null;

    CreateProfileViewModel createProfileViewModel;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateProfileBinding.inflate(getLayoutInflater());
        audioManager=(AudioManager) getSystemService(AUDIO_SERVICE);

        Bundle bundle = getIntent().getBundleExtra(this.getString(R.string.bundle_profile_obj));
        if (bundle != null) {
            profile = (Profile) bundle.getSerializable(getString(R.string.arg_profile_obj));
            String id = String.valueOf(profile.getProfileId());
            binding.textTitle.setText(profile.getTitle());
        }
        if(profile!=null) {
            startHour = profile.getStartHour();
            startMinute = profile.getStartMinute();
            endHour = profile.getEndHour();
            endMinute = profile.getEndMinute();
        }

        createProfileViewModel= new ViewModelProvider(this).get(CreateProfileViewModel.class);

        binding.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int startM=21,startH=30;
                if(profile!=null)
                {
                    startH=profile.getStartHour();
                    startM=profile.getStartMinute();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateProfileActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    startHour = hourOfDay;
                                    startMinute = minute;
                            }
                        },
                        startH, // Default hour (e.g., current hour)
                        startM, // Default minute (e.g., current minute)
                        true // Set to true if you want to use 24-hour format, false for AM/PM format
                );
                timePickerDialog.show();
            }
        });
        binding.endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int endM=21,endH=30;
                if(profile!=null)
                {
                    endHour=endH=profile.getEndHour();
                    endMinute=endM=profile.getEndMinute();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateProfileActivity.this,
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
                        endH, // Default hour (e.g., current hour)
                        endM, // Default minute (e.g., current minute)
                        true // Set to true if you want to use 24-hour format, false for AM/PM format
                );
                timePickerDialog.show();
            }
        });

        binding.seekBarRing.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        if(profile!=null)
        {
            binding.seekBarRing.setProgress(profile.getRingVol());
        }else {
            binding.seekBarRing.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
        }

        binding.seekBarRing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ringVol=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        if(profile!=null)
        {
            binding.seekBarVolume.setProgress(profile.getMediaVol());
        } else {
            binding.seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }

        binding.seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaVol=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    title=binding.textTitle.getText().toString();

                if(checkTime()) {
                        if (profile != null) {
                            updateProfile();
                        } else {
                            scheduleProfile();
                        }
                } else{
                    Toast.makeText(CreateProfileActivity.this,"Start time must be less then end time",Toast.LENGTH_SHORT).show();
                }
            }

        });
        setContentView(binding.getRoot());
    }

    private void updateProfile() {
        Profile newProfile =new Profile(profile.getProfileId(),title,startHour,startMinute,endHour,endMinute,profile.isStarted(),mediaVol,ringVol);
        createProfileViewModel.update(newProfile);
    }

    public void scheduleProfile(){
        int profileId = new Random().nextInt(1000);
        Profile profile=new Profile(profileId,title,startHour,startMinute,endHour,endMinute,false, mediaVol, ringVol);
        profile.schedule(this,profile);
        createProfileViewModel.insert(profile);
    }
    public boolean checkTime()
    {
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
        }
        else{
            return true;
        }
    }
}
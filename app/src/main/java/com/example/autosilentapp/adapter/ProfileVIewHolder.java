package com.example.autosilentapp.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosilentapp.R;
import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.databinding.ItemProfileBinding;
import com.example.autosilentapp.util.DayUtil;
import com.example.autosilentapp.util.OnToggleSessionListener;
import com.example.autosilentapp.util.OnToggleSessionListenerProfile;

public class ProfileVIewHolder  extends RecyclerView.ViewHolder{

    private TextView profileStartTime,profileEndTime;
    private ImageView profileRecurring;
    private TextView profileRecurringDays;
    private TextView profileTitle;
    private Switch profileStarted;
    private ImageButton deleteProfile;
    private View itemView;
    private TextView profileDay;

    public ProfileVIewHolder(@NonNull ItemProfileBinding itemProfileBinding) {
        super(itemProfileBinding.getRoot());
        profileStartTime = itemProfileBinding.itemSessionStartTime;
        profileEndTime=itemProfileBinding.itemSessionEndTime;
        profileStarted = itemProfileBinding.itemAlarmStarted;
        profileTitle= itemProfileBinding.itemAlarmTitle;
        deleteProfile= itemProfileBinding.itemAlarmRecurringDelete;
        profileDay = itemProfileBinding.itemAlarmDay;
        profileRecurring=itemProfileBinding.itemAlarmRecurring;
        profileRecurringDays=itemProfileBinding.itemAlarmRecurringDays;
        this.itemView=itemProfileBinding.getRoot();
    }
    public void bind(Profile profile, OnToggleSessionListenerProfile listener) {
        String startSessionText = String.format("%02d:%02d", profile.getStartHour(), profile.getStartMinute());
        String endSessionText = String.format("%02d:%02d", profile.getEndHour(), profile.getEndMinute());

        profileStartTime.setText(startSessionText);
        profileEndTime.setText(endSessionText);
        profileStarted.setChecked(profile.isStarted());


            profileRecurring.setImageResource(R.drawable.ic_looks_one_black);
            profileRecurringDays.setText("Once Off");

        profileTitle.setText(profile.getTitle());

            profileDay.setText(DayUtil.getDay(profile.getStartHour(),profile.getStartMinute()));
        profileStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isShown() || buttonView.isPressed())
                    listener.onToggle(profile);
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(profile);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(profile,view);
            }
        });
    }
}

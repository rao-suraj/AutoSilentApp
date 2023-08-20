package com.example.autosilentapp.presentation.list_profile.recycler_view;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosilentapp.R;
import com.example.autosilentapp.database.local.model.Profile;
import com.example.autosilentapp.databinding.ItemProfileBinding;
import com.example.autosilentapp.util.DayUtil;
import com.example.autosilentapp.util.OnToggleSessionListenerProfile;

public class ProfileVIewHolder  extends RecyclerView.ViewHolder{

    private TextView profileStartTime1,profileEndTime1,profileStartTime2,profileEndTIme2;
    private ImageView profileRecurring;
    private TextView profileRecurringDays;
    private TextView profileTitle;
    private Switch profileStarted;
    private ImageButton deleteProfile;
    private View itemView;
    private TextView profileDay;

    public ProfileVIewHolder(@NonNull ItemProfileBinding itemProfileBinding) {
        super(itemProfileBinding.getRoot());
        profileStartTime1 = itemProfileBinding.startTxt1;
        profileEndTime1=itemProfileBinding.txtEnd1;
        profileStartTime2=itemProfileBinding.startText2;
        profileEndTIme2=itemProfileBinding.textEnd2;
        profileStarted = itemProfileBinding.itemAlarmStarted;
        profileTitle= itemProfileBinding.itemAlarmTitle;
        deleteProfile= itemProfileBinding.itemAlarmRecurringDelete;
        profileDay = itemProfileBinding.itemAlarmDay;
        profileRecurring=itemProfileBinding.itemAlarmRecurring;
        profileRecurringDays=itemProfileBinding.itemAlarmRecurringDays;
        this.itemView=itemProfileBinding.getRoot();
    }
    public void bind(Profile profile, OnToggleSessionListenerProfile listener) {
        String startSessionText1 = String.format("%02d", profile.getStartHour());
        String startSessionText2 = String.format(":%02d", profile.getStartMinute());
        String endSessionText1 = String.format("%02d", profile.getEndHour());
        String endSessionText2 = String.format(":%02d", profile.getEndMinute());

        profileStartTime1.setText(startSessionText1);
        profileStartTime2.setText(startSessionText2);
        profileEndTime1.setText(endSessionText1);
        profileEndTIme2.setText(endSessionText2);
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

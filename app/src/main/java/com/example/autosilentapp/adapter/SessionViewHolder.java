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
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.databinding.ItemSessionBinding;
import com.example.autosilentapp.util.DayUtil;
import com.example.autosilentapp.util.OnToggleSessionListener;

public class SessionViewHolder extends RecyclerView.ViewHolder{
    private TextView sessionStartTime,sessionEndTime;
    private ImageView sessionRecurring;
    private TextView sessionRecurringDays;
    private TextView sessionTitle;
    private Switch sessionStarted;
    private ImageButton deleteSession;
    private View itemView;
    private TextView sessionDay;



    public SessionViewHolder(@NonNull ItemSessionBinding itemSessionBinding) {
        super(itemSessionBinding.getRoot());
        sessionStartTime = itemSessionBinding.itemSessionStartTime;
        sessionEndTime=itemSessionBinding.itemSessionEndTime;
        sessionStarted = itemSessionBinding.itemAlarmStarted;
        sessionRecurring = itemSessionBinding.itemAlarmRecurring;
        sessionRecurringDays = itemSessionBinding.itemAlarmRecurringDays;
        sessionTitle = itemSessionBinding.itemAlarmTitle;
        deleteSession= itemSessionBinding.itemAlarmRecurringDelete;
        sessionDay = itemSessionBinding.itemAlarmDay;
        this.itemView=itemSessionBinding.getRoot();
    }
    public void bind(Session session, OnToggleSessionListener listener) {
        String startSessionText = String.format("%02d:%02d", session.getStartHour(), session.getStartMinute());
        String endSessionText = String.format("%02d:%02d", session.getEndHour(), session.getEndMinute());

        sessionStartTime.setText(startSessionText);
        sessionEndTime.setText(endSessionText);
        sessionStarted.setChecked(session.isStarted());

        if (session.isRecurring()) {
            sessionRecurring.setImageResource(R.drawable.ic_repeat_black);
            sessionRecurringDays.setText(session.getRecurringDaysText());
        } else {
            sessionRecurring.setImageResource(R.drawable.ic_looks_one_black);
            sessionRecurringDays.setText("Once Off");
        }


            sessionTitle.setText("My alarm");
        if(session.isRecurring()){
            sessionDay.setText(session.getRecurringDaysText());
        }
        else {
            sessionDay.setText(DayUtil.getDay(session.getStartHour(),session.getStartMinute()));
        }
        sessionStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isShown() || buttonView.isPressed())
                    listener.onToggle(session);
            }
        });

        deleteSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(session);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(session,view);
            }
        });
    }
}

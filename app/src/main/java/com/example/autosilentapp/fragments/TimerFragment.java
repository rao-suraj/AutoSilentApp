package com.example.autosilentapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.autosilentapp.CreateSessionActivity;
import com.example.autosilentapp.R;
import com.example.autosilentapp.adapter.SessionRecyclerViewAdapter;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.databinding.FragmentTimerBinding;
import com.example.autosilentapp.util.OnToggleSessionListener;
import com.example.autosilentapp.viewmodel.CreateSessionViewModel;
import com.example.autosilentapp.viewmodel.SessionListViewModel;

import java.util.List;

public class TimerFragment extends Fragment implements OnToggleSessionListener {
    Session session;
    private SessionRecyclerViewAdapter sessionRecyclerViewAdapter;
    private SessionListViewModel sessionListViewModel;
    private RecyclerView sessionsRecyclerView;
    private FragmentTimerBinding fragmentTimerBinding;
    private List<Session> sec;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionRecyclerViewAdapter = new SessionRecyclerViewAdapter(this);
        sessionListViewModel=new ViewModelProvider(this).get(SessionListViewModel.class);
        sessionListViewModel.getSessionLiveData().observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {
                sessionListViewModel = new ViewModelProvider(TimerFragment.this).get(SessionListViewModel.class);
                sessionListViewModel.getSessionLiveData().observe(TimerFragment.this, new Observer<List<Session>>() {
                    @Override
                    public void onChanged(List<Session> sessions) {
                        if (sessions != null) {
                            sessionRecyclerViewAdapter.setSessions(sessions);
                        }
                    }
                });
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentTimerBinding = FragmentTimerBinding.inflate(inflater, container, false);
        sessionsRecyclerView = fragmentTimerBinding.fragmentListsessionRecylerView;
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sessionsRecyclerView.setAdapter(sessionRecyclerViewAdapter);

//        Session sec=new Session(22,50,22,55,false,true,false,false,false,false,false,true,false,null);
//        sec.setStartHour(21);
//        sec.setEndHour(21);
//        sec.setStartMinute(29);
//        sec.setEndMinute(30);
//        sec.setStarted(false);
//        sec.setRecurring(true);
//        sec.setMonday(false);
//        sec.setTuesday(false);
//        sec.setWednesday(false);
//        sec.setThursday(false);
//        sec.setFriday(false);
//        sec.setSaturday(true);
//        sec.setSunday(false);
//        sec.schedule(getContext());
        return fragmentTimerBinding.getRoot();
    }

    @Override
    public void onToggle(Session session) {
        if (session.isStarted()) {
            session.cancelAlarm(getContext(),session);
            sessionListViewModel.update(session);
        } else {
            session.schedule(getContext(),session);
            sessionListViewModel.update(session);
        }
    }

    @Override
    public void onDelete(Session session) {
        if (session.isStarted())
            session.cancelAlarm(getContext(),session);
        sessionListViewModel.delete(session.getSessionId());
    }

    @Override
    public void onItemClick(Session session,View view) {
//        if (alarm.isStarted())
//            alarm.cancelAlarm(getContext());
//        Bundle args = new Bundle();
//        args.putSerializable(getString(R.string.arg_alarm_obj),alarm);
//        Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment,args);

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.arg_alarm_obj), session);

        TimerFragment timerFragment = new TimerFragment();
        timerFragment.setArguments(bundle);

        Intent intent = new Intent(getActivity(), CreateSessionActivity.class);
        intent.putExtra(this.getString(R.string.bundle_alarm_obj), bundle);
        startActivity(intent);
    }
}
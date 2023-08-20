package com.example.autosilentapp.presentation.list_session;

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

import com.example.autosilentapp.presentation.create_session.CreateSessionActivity;
import com.example.autosilentapp.R;
import com.example.autosilentapp.presentation.list_session.recyeler_view.SessionRecyclerViewAdapter;
import com.example.autosilentapp.database.local.model.Session;
import com.example.autosilentapp.databinding.FragmentTimerBinding;
import com.example.autosilentapp.util.OnToggleSessionListener;

import java.util.List;

public class SessionFragment extends Fragment implements OnToggleSessionListener {
    Session session;
    private SessionRecyclerViewAdapter sessionRecyclerViewAdapter;
    private SessionListViewModel sessionListViewModel;
    private RecyclerView sessionsRecyclerView;
    private FragmentTimerBinding fragmentTimerBinding;
    private List<Session> sec;

    public SessionFragment() {
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
                sessionListViewModel.getSessionLiveData().observe(SessionFragment.this, new Observer<List<Session>>() {
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

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.arg_alarm_obj), session);

        SessionFragment timerFragment = new SessionFragment();
        timerFragment.setArguments(bundle);

        Intent intent = new Intent(getActivity(), CreateSessionActivity.class);
        intent.putExtra(this.getString(R.string.bundle_alarm_obj), bundle);
        startActivity(intent);
    }
}
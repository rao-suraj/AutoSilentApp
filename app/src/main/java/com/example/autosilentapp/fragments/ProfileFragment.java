package com.example.autosilentapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.autosilentapp.CreateProfileActivity;
import com.example.autosilentapp.CreateSessionActivity;
import com.example.autosilentapp.R;
import com.example.autosilentapp.adapter.ProfileRecycleViewAdapter;
import com.example.autosilentapp.adapter.SessionRecyclerViewAdapter;
import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.databinding.FragmentProfileBinding;
import com.example.autosilentapp.databinding.FragmentTimerBinding;
import com.example.autosilentapp.repository.ProfileRepository;
import com.example.autosilentapp.util.OnToggleSessionListenerProfile;
import com.example.autosilentapp.viewmodel.ProfileListViewModel;
import com.example.autosilentapp.viewmodel.SessionListViewModel;

import java.util.List;


public class ProfileFragment extends Fragment implements OnToggleSessionListenerProfile {

    Profile profile;
    private ProfileRecycleViewAdapter profileRecycleViewAdapter;
    private ProfileListViewModel profileListViewModel;
    private RecyclerView profileRecycleView;
    private FragmentProfileBinding fragmentProfileBinding;
    private List<Profile> prof;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileRecycleViewAdapter=new ProfileRecycleViewAdapter(this);
        profileListViewModel=new ViewModelProvider(this).get(ProfileListViewModel.class);
        profileListViewModel.getProfileLiveData().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                profileRecycleViewAdapter.setProfiles(profiles);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);

        profileRecycleView = fragmentProfileBinding.fragmentListalarmsRecylerView;
        profileRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileRecycleView.setAdapter(profileRecycleViewAdapter);
        return fragmentProfileBinding.getRoot();
    }

    @Override
    public void onToggle(Profile profile) {
        if (profile.isStarted()) {
            profile.cancelAlarm(getContext(),profile);
            profileListViewModel.update(profile);
        } else {
            profile.schedule(getContext(),profile);
            profileListViewModel.update(profile);
        }
    }

    @Override
    public void onDelete(Profile profile) {
        if (profile.isStarted())
            profile.cancelAlarm(getContext(),profile);
        profileListViewModel.delete(profile.getProfileId());
    }

    @Override
    public void onItemClick(Profile profile, View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.arg_profile_obj), profile);

        TimerFragment timerFragment = new TimerFragment();
        timerFragment.setArguments(bundle);

        Intent intent = new Intent(getActivity(), CreateProfileActivity.class);
        intent.putExtra(this.getString(R.string.bundle_profile_obj), bundle);
        startActivity(intent);
    }
}
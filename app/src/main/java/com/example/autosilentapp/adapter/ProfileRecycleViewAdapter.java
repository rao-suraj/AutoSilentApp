package com.example.autosilentapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.databinding.ItemProfileBinding;

import com.example.autosilentapp.util.OnToggleSessionListenerProfile;

import java.util.ArrayList;
import java.util.List;

public class ProfileRecycleViewAdapter extends RecyclerView.Adapter<ProfileVIewHolder>{

    private List<Profile> profiles;
    private OnToggleSessionListenerProfile listener;
    private ItemProfileBinding binding;

    public ProfileRecycleViewAdapter(OnToggleSessionListenerProfile listener) {
        this.profiles = new ArrayList<Profile>();
        this.listener = listener;
    }
    @NonNull
    @Override
    public ProfileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProfileBinding view= ItemProfileBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProfileVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileVIewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.bind(profile, listener);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }
    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }
}

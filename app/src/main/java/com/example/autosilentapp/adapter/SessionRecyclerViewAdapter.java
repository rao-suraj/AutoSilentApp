package com.example.autosilentapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosilentapp.R;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.databinding.ItemSessionBinding;
import com.example.autosilentapp.util.OnToggleSessionListener;

import java.util.ArrayList;
import java.util.List;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter<SessionViewHolder>{
    private List<Session> sessions;
    private OnToggleSessionListener listener;
    private ItemSessionBinding binding;

    public SessionRecyclerViewAdapter(OnToggleSessionListener listener) {
        this.sessions = new ArrayList<Session>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSessionBinding view= ItemSessionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.bind(session, listener);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }
}

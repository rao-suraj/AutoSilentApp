package com.example.autosilentapp.util;

import android.view.View;

import com.example.autosilentapp.database.local.model.Profile;
public interface OnToggleSessionListenerProfile {
    void onToggle(Profile profile);
    void onDelete(Profile profile);
    void onItemClick(Profile profile, View view);
}

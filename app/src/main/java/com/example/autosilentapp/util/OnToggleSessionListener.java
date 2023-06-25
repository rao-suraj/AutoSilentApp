package com.example.autosilentapp.util;

import android.view.View;

import com.example.autosilentapp.database.Session;

public interface OnToggleSessionListener {
    void onToggle(Session session);
    void onDelete(Session session);
    void onItemClick(Session alarm, View view);
}

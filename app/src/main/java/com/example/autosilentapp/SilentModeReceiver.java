package com.example.autosilentapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class SilentModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (intent != null && audioManager != null) {
            String action = intent.getAction();

            if (action != null) {
                switch (action) {
                    case "START_SILENT_MODE":
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        Toast.makeText(context, "Silent mode activated", Toast.LENGTH_SHORT).show();
                        break;
                    case "END_SILENT_MODE":
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        Toast.makeText(context, "Silent mode deactivated", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }
}

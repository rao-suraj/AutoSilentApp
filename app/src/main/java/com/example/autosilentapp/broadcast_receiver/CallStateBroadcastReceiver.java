package com.example.autosilentapp.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;


public class CallStateBroadcastReceiver extends BroadcastReceiver {

    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            // Retrieve the call state from the intent
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            // Check the call state and handle it accordingly
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // Call has ended.
                Toast.makeText(context,"Phone call ended",Toast.LENGTH_SHORT).show();
               setSilentMode();
            }
            // Check the call is in ringing.
            if(state !=null & state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                Toast.makeText(context,"Phone call Ringing",Toast.LENGTH_SHORT).show();
               setNormalMode();
            }
        }
    }

    // Bring the phone to normal mode
    private void setNormalMode() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); // normal
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 7,
                AudioManager.FLAG_SHOW_UI
                        + AudioManager.FLAG_PLAY_SOUND);
    }

    //back to Vibration mode
    private void setSilentMode() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
    }
}

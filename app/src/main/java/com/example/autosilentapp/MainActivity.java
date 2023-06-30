package com.example.autosilentapp;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.databinding.ActivityMainBinding;
import com.example.autosilentapp.fragments.ProfileFragment;
import com.example.autosilentapp.fragments.TimerFragment;
import com.example.autosilentapp.repository.ProfileRepository;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;

    int REQUEST_READ_PHONE_STATE=1;
    int REQUEST_DND_CODE=2;
    private static final int REQUEST_MODIFY_AUDIO_SETTINGS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkForPermission();

        fragmentManager= getSupportFragmentManager();

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.timer:
                    replaceFragment(new TimerFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
        replaceFragment(new TimerFragment());
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,CreateSessionActivity.class);
                    startActivity(intent);
            }
        });
    }

    private void checkForPermission() {
        checkCallStatePermission();
        askDNDPermission();
        checkModifyAudioSettingsPermission();
    }


    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void checkCallStatePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        }
    }

    private void askDNDPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it from the user
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, REQUEST_DND_CODE);
        }
    }
    private void checkModifyAudioSettingsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                    REQUEST_MODIFY_AUDIO_SETTINGS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_PHONE_STATE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(this,"PHONE_STATE_GRANTED",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"PHONE_STATE_NOT_GRANTED",Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_DND_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"DND_STATE_GRANTED",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"DND_NOT_GRANTED",Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_MODIFY_AUDIO_SETTINGS) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"MODIFY_AUDIO_GRANTED",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"MODIFY_AUDIO_NOT_GRANTED",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
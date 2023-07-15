package com.example.autosilentapp;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.databinding.ActivityMainBinding;
import com.example.autosilentapp.fragments.ProfileFragment;
import com.example.autosilentapp.fragments.TimerFragment;
import com.example.autosilentapp.repository.ProfileRepository;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    NotificationManager notificationManager ;

    int REQUEST_READ_PHONE_STATE=3;
    int REQUEST_DND_CODE=2;
    private static final int REQUEST_MODIFY_AUDIO_SETTINGS = 1;

    private static final int ACCESS_NOTIFICATION_POLICY_REQUEST_CODE = 123;

    private static final int ACCESS_POST_NOTIFICATION_PERMISSION=22;

    Dialog dialogAbout,dialogFeedback,dialogAdd;

    Button addSession,addProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen=SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notificationManager = getSystemService(NotificationManager.class);


        //Dialog Initialization
        initializeDialog();

        addSession=dialogAdd.findViewById(R.id.add_session);
        addProfile=dialogAdd.findViewById(R.id.add_profile);

        // Transparent StatusBar
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //Toolbar
        Toolbar toolbar=binding.toolBar;
        setSupportActionBar(toolbar);


        //check permission
        checkForPermission();

        //fragmentInitialization
        fragmentManager= getSupportFragmentManager();
        //fragmentBackground
        binding.bottomNavigationView.setBackground(null);

        //fragmentSwitching
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
                    dialogAdd.show();
            }
        });

        addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CreateSessionActivity.class);
                startActivity(intent);
            }
        });

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CreateProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initializeDialog() {
        dialogAbout= new Dialog(MainActivity.this);
        dialogAbout.setContentView(R.layout.about_layout);
        dialogAbout.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAbout.getWindow().setBackgroundDrawable(getDrawable(R.drawable.about_bg));

        dialogFeedback= new Dialog(MainActivity.this);
        dialogFeedback.setContentView(R.layout.feedback_layout);
        dialogAbout.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAbout.getWindow().setBackgroundDrawable(getDrawable(R.drawable.about_bg));

        dialogAdd= new Dialog(MainActivity.this);
        dialogAdd.setContentView(R.layout.add_layout);
        dialogAbout.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAbout.getWindow().setBackgroundDrawable(getDrawable(R.drawable.about_bg));

    }

    private void checkForPermission() {
        askNotificationPermission();
        checkAccessNotificationPolicyPermission();
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

    private void askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, ACCESS_POST_NOTIFICATION_PERMISSION);
        }
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

    public void checkAccessNotificationPolicyPermission() {
        // Check if ACCESS_NOTIFICATION_POLICY permission is granted

        if (notificationManager.isNotificationPolicyAccessGranted()) {
            // ACCESS_NOTIFICATION_POLICY permission is granted
            Toast.makeText(this, "ACCESS_NOTIFICATION_POLICY permission is already granted", Toast.LENGTH_SHORT).show();
        } else {
            // ACCESS_NOTIFICATION_POLICY permission is not granted, prompt the user to enable it
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, ACCESS_NOTIFICATION_POLICY_REQUEST_CODE);
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
        if(requestCode == ACCESS_NOTIFICATION_POLICY_REQUEST_CODE) {
            // Check if the user has granted ACCESS_NOTIFICATION_POLICY permission after being prompte
            if (notificationManager.isNotificationPolicyAccessGranted()) {
//                Toast.makeText(this, "ACCESS_NOTIFICATION_POLICY permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ACCESS_NOTIFICATION_POLICY permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == ACCESS_POST_NOTIFICATION_PERMISSION) {
            // Check if the user has granted ACCESS_NOTIFICATION_POLICY permission after being prompte
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"NOTIFICATION_GRANTED",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"NOTIFICATION_NOT_GRANTED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.opt_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id=item.getItemId();

        if(item_id==R.id.opt_about)
        {
            dialogAbout.show();
        }
        if(item_id==R.id.opt_feedback)
        {
            dialogFeedback.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
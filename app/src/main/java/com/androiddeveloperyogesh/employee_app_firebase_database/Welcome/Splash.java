package com.androiddeveloperyogesh.employee_app_firebase_database.Welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.androiddeveloperyogesh.employee_app_firebase_database.EmployeeRelated.AllEmployees;
import com.androiddeveloperyogesh.employee_app_firebase_database.MainActivity;
import com.androiddeveloperyogesh.employee_app_firebase_database.R;
import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {
    ActivitySplashBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

        new CountDownTimer(2600, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                intent = new Intent(Splash.this, AllEmployees.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
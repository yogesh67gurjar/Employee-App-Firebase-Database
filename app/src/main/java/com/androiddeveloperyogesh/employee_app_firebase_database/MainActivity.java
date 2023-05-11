package com.androiddeveloperyogesh.employee_app_firebase_database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androiddeveloperyogesh.employee_app_firebase_database.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}
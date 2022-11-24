package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.timerapp.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {
        ActivityNotificationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
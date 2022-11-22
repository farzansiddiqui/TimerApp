package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;


import com.example.timerapp.animation.ViewAnimation;
import com.example.timerapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    boolean statusCheck = false;
    boolean isRotate = false;
    public static final String TAG = "Value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: ");
        handler = new Handler();

        binding.resetImage.setOnClickListener(view -> {
            MillisecondTime = 0L;
            StartTime = 0L;
            TimeBuff = 0L;
            UpdateTime = 0L;
            Seconds = 0;
            Minutes = 0;
            MilliSeconds = 0;
            binding.resetImage.setVisibility(View.GONE);


            binding.minutes.setText(getResources().getText(R.string.minute));
            binding.seconds.setText(getResources().getText(R.string.seconds));
            binding.milliSeconds.setText(getResources().getText(R.string.milli_seconds));
        });


        binding.playBtn.setOnClickListener(view -> {

            if (!statusCheck) {
                StartTime = SystemClock.uptimeMillis();
                isRotate = ViewAnimation.rotateFab(view, !isRotate);
                Log.d(TAG, "onCreate: "+StartTime);
                handler.postDelayed(runnable, 0);
                binding.resetImage.setVisibility(View.GONE);
                binding.playBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_baseline_pause_24));
                statusCheck = true;

            } else {
                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);
                isRotate = ViewAnimation.rotateFab(view, !isRotate);
                binding.resetImage.setVisibility(View.VISIBLE);
                binding.playBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_baseline_play_arrow_24));
                statusCheck = false;
            }
        });
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            binding.minutes.setText("0"+Minutes);
            binding.seconds.setText(":"+String.format("%02d", Seconds));
            binding.milliSeconds.setText(":"+String.format("%03d", MilliSeconds));
           // binding.showTimer.setText("0" + Minutes + ":" + String.format("%02d", Seconds) + ":" + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);


        }
    };
}
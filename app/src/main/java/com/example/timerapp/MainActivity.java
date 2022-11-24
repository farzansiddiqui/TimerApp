package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
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
    String textTitle = "Title";
    String textContent = "Description about application";
    public static final String CHANNEL_ID = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: ");
        handler = new Handler();

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).
                setContentIntent(pendingIntent).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, builder.build());
        createNotification();

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

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
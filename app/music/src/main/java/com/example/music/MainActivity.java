package com.example.music;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.PendingIntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    Context context;
    SeekBar seekBar;
    boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        seekBar = findViewById(R.id.seekBar);
        String imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqYZEnyZhBgJqe_8knHYkZoqRylTwNzQROmw&s";


        MyThread myThread = new MyThread(imageUrl, context);
        myThread.start();

        button.setOnClickListener(view -> {
            if (isPlaying) {
                if (MyService.mediaPlayer != null && MyService.mediaPlayer.isPlaying()) {
                    MyService.mediaPlayer.pause();
                    isPlaying = false;
                    button.setText("Play");
                }
            } else {
                startService(new Intent(this, MyService.class));
                isPlaying = true;
                button.setText("Pause");
                updateSeekBar();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && MyService.mediaPlayer != null){
                    MyService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSeekBar();
    }

    private void updateSeekBar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MyService.mediaPlayer != null && MyService.mediaPlayer.isPlaying()) {
                    isPlaying = true;
                    seekBar.setMax(MyService.mediaPlayer.getDuration());
                    seekBar.setProgress(MyService.mediaPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying && ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //1. Khoi tao Intent la MainActivity
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            //2. Chuyen sang PendingIntent de dua cho Noti
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            //3. Khoi tao thong bao
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Music")
                    .setContentText("Nhac dang phat")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(null)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            //5. Hien thi thong bao
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}
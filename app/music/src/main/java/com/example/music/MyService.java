package com.example.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.security.Provider;

public class MyService extends Service {
    static MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.efn);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1. Tao Noti Channel
        NotificationChannel channel = new NotificationChannel("noti_channel_id", "My Service Channel", NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        // 2. Tao Noti
        Notification notification = new NotificationCompat.Builder(this, "noti_channel_id")
                .setContentTitle("Dang chay ngam")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        // 3. Chi dinh la Foreground Service
        startForeground(1, notification);
        mediaPlayer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }
}

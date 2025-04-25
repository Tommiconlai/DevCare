package com.mypackage.devcare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundTimerService extends Service {

    private CountDownTimer currentTimer;
    private boolean runningPrimary = true;
    private long primaryDuration = 45 * 60 * 1000;
    private long secondaryDuration = 15 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        startInForeground();
        startPrimaryTimer();
    }

    private void startInForeground() {
        String channelId = "TIMER_CHANNEL";
        String channelName = "Timer Foreground Service";

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Timer attivo")
                .setContentText("Il timer è in esecuzione...")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Sostituisci con una tua icona
                .setOngoing(true)
                .build();

        startForeground(1, notification);
    }

    private void startPrimaryTimer() {
        currentTimer = new CountDownTimer(primaryDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Notifica aggiornata o messaggi broadcast
            }

            @Override
            public void onFinish() {
                playBeep();
                runningPrimary = false;
                startSecondaryTimer();
            }
        }.start();
    }

    private void startSecondaryTimer() {
        currentTimer = new CountDownTimer(secondaryDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Notifica aggiornata o messaggi broadcast
            }

            @Override
            public void onFinish() {
                playBeep();
                runningPrimary = true;
                startPrimaryTimer();
            }
        }.start();
    }

    private void playBeep() {
        MediaPlayer player = MediaPlayer.create(this, R.raw.beep);
        player.setOnCompletionListener(MediaPlayer::release);
        player.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Mantiene il service vivo il più possibile
    }

    @Override
    public void onDestroy() {
        if (currentTimer != null) currentTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

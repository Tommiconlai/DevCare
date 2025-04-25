package com.mypackage.devcare;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CountDownTimer primaryTimer;
    private CountDownTimer secondaryTimer;
    private boolean isRunning = false;
    private long primaryDuration = 45 * 60 * 1000;
    private long secondaryDuration = 15 * 60 * 1000;
    TextView firstTimerView, secondTimerView;
    ImageButton btnStart;
    MediaPlayer beepSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTimerView = findViewById(R.id.forstTimer);
        firstTimerView.setText(primaryDuration / 1000 / 60 + ":" + primaryDuration / 1000 % 60 + "0");
        secondTimerView = findViewById(R.id.secondTimer);
        secondTimerView.setText(secondaryDuration / 1000 / 60 + ":" + secondaryDuration / 1000 % 60 + "0");
        btnStart = findViewById(R.id.btnStart);

        beepSound = MediaPlayer.create(this, R.raw.beep);

        btnStart.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                startPrimaryTimer();
            } else {
                stopTimers();
            }
        });
    }

    private void startPrimaryTimer() {
        primaryTimer = new CountDownTimer(primaryDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                updateTimerUI(firstTimerView, millisUntilFinished);
            }

            public void onFinish() {
                beepSound.start();
                startSecondaryTimer();
            }
        }.start();
    }

    private void startSecondaryTimer() {
        secondaryTimer = new CountDownTimer(secondaryDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                updateTimerUI(secondTimerView, millisUntilFinished);
            }

            public void onFinish() {
                beepSound.start();
                startPrimaryTimer(); // loop!
            }
        }.start();
    }

    private void updateTimerUI(TextView timerView, long millis) {
        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerView.setText(timeFormatted);
    }

    private void stopTimers() {
        if (primaryTimer != null) primaryTimer.cancel();
        if (secondaryTimer != null) secondaryTimer.cancel();
        isRunning = false;
    }
}
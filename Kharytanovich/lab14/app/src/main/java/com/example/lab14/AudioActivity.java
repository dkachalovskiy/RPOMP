package com.example.lab14;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AudioActivity extends AppCompatActivity {
    private MediaPlayer mPlayer;
    private Button startButton,
            pauseButton,
            stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        startButton = findViewById(R.id.audio_startButton);
        pauseButton = findViewById(R.id.audio_pauseButton);
        stopButton = findViewById(R.id.audio_stopButton);

        mPlayer = MediaPlayer.create(this, getIntent().getData());
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        mPlayer.setOnCompletionListener(mp -> stopPlay());
        onStartClick(startButton);
    }

    public void onStartClick(View view) {
        mPlayer.start();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    public void onPauseClick(View view) {
        mPlayer.pause();
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void onStopClick(View view) {
        stopPlay();
    }

    private void stopPlay() {
        mPlayer.stop();
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
            startButton.setEnabled(true);
        } catch (Throwable t) {
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }
}
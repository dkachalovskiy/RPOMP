package com.example.lab_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    //для установки разрешений
    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private boolean permissionGranted;
    //для элементов интерфейса
    private MediaPlayer mPlayer;
    private Button startButton, pauseButton, stopButton;
    private VideoView videoView;
    private String setType;
    private ImageView imageView;
    //для файлового менеджера
    private static final int PICKFILE_RESULT_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //проверка и установка разрешений
        if(!permissionGranted){
//            checkPermissions();
        }
        //получение ссылок на элементы интерфейса
        startButton = (Button) findViewById(R.id.start);
        pauseButton = (Button) findViewById(R.id.pause);
        stopButton = (Button) findViewById(R.id.stop);
        videoView =(VideoView) findViewById(R.id.videoView);
    }
    boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    private boolean checkPermissions(){
        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new
                            String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int RequestCode, @NonNull String[] permissions,
                                       @NonNull int grantResults[]) {
        super.onRequestPermissionsResult(RequestCode, permissions, grantResults);
        switch (RequestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "Разрешения получены", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Необходимо дать разрешения", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onClFile(View viewButton) {
        //определим, какая кнопка нажата
        if (viewButton.getId()==R.id.buttonAudio){
            setType="audio/*";
        }
        if (viewButton.getId()==R.id.buttonVideo){
            setType=setType="video/*";
        }
        if (viewButton.getId()==R.id.buttonImage){
            setType=setType="image/*";
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(setType); //определяем тип
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK){
            //если выбран аудиофайл
            if ( setType=="audio/*"){ //если выбран аудиофайл
                mPlayer=MediaPlayer.create(this, data.getData());
                mPlayer.start();
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlay();
                    }
                });
            }
            if ( setType=="video/*") { //если выбран видеофайл
                videoView.setVideoURI(data.getData());//
                videoView.start();
            }
            if ( setType=="image/*") { //если выбрано изображение
                setContentView(R.layout.activity_image); //включение разметки с imageView
                imageView =(ImageView) findViewById(R.id.imageView);
                imageView.setImageURI(data.getData());
                View.OnClickListener d = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_main);
                    }
                };
                imageView.setOnClickListener(d);
            }
        }
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        super.onBackPressed();
        setContentView(R.layout.activity_main);
    }
    public void Back() {
        setContentView(R.layout.activity_main);
    }
    public void play(View view){ //обработчик кнопки PLAY
        if ( setType=="audio/*"){ //если выбран аудиофайл
            mPlayer.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }
        if ( setType=="video/*") { //если выбран видеофайл
            videoView.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }

    }
    public void pause(View view){ //обработчик кнопки PAUSE

        if ( setType=="audio/*"){ //если выбран аудиофайл
            mPlayer.pause();
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
        if ( setType=="video/*") { //если выбран видеофайл
            videoView.pause();
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }
    public void stop(View view){ //обработчик кнопки STOP
        stopPlay();
    }
    private void stopPlay(){ //остановка воспроизведения аудио
        if ( setType=="audio/*"){ //если выбран аудиофайл
            mPlayer.stop();
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            try {
                mPlayer.prepare();
                mPlayer.seekTo(0);
                startButton.setEnabled(true);
            }
            catch (Throwable t) {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if ( setType=="video/*") { //если выбран видеофайл
            videoView.stopPlayback();
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        }

    }




}
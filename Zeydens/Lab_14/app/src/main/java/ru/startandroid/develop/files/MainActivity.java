package ru.startandroid.develop.files;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

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
            checkPermissions();
        }
        //получение ссылок на элементы интерфейса
        startButton = (Button) findViewById(R.id.start);
        pauseButton = (Button) findViewById(R.id.pause);
        stopButton = (Button) findViewById(R.id.stop);
        videoView =(VideoView) findViewById(R.id.videoView);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        startButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(v);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause(v);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(v);
            }
        });

        Button buttonVideo = findViewById(R.id.buttonVideo);
        Button buttonImage = findViewById(R.id.buttonImage);
        Button buttonAudio = findViewById(R.id.buttonAudio);

        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClFile(v);
            }
        });

        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClFile(v);
            }
        });

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClFile(v);
            }
        });
    }
    //проверка и установка разрешений для работы с внешним хранилищем
// проверяем, доступно ли внешнее хранилище для чтения и записи
    //проверка и установка разрешений для работы с внешним хранилищем
// проверяем, доступно ли внешнее хранилище для чтения и записи
    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    private boolean checkPermissions(){
        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
    String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "Разрешения получены",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Необходимо дать разрешения",
                            Toast.LENGTH_LONG).show();
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
                startButton.setEnabled(true);
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
                startButton.setEnabled(true);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
            }
            if ( setType=="image/*") { //если выбрано изображение
                setContentView(R.layout.activity_image_view); //включение разметки с imageView
                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageURI(data.getData());
                Button btnToMainFromImg = findViewById(R.id.btnToMainFromImg);
                btnToMainFromImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Вернуться назад к предыдущей активности
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void play(View view) { // Обработчик кнопки PLAY
        if (setType.equals("audio/*")) {
            // Воспроизвести аудио
            mPlayer.start();

        } else if (setType.equals("video/*")) {
            // Воспроизвести видео
            videoView.start();
        }
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    public void pause(View view) { // Обработчик кнопки PAUSE
        if (setType.equals("audio/*")) {
            // Приостановить аудио
            mPlayer.pause();

        } else if (setType.equals("video/*")) {
            // Приостановить видео
            videoView.pause();
        }
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void stop(View view) { // Обработчик кнопки STOP
        if (setType.equals("audio/*")) {
            // Остановить воспроизведение аудио
            stopPlay();
        } else if (setType.equals("video/*")) {
            // Остановить воспроизведение видео
            videoView.stopPlayback();
            videoView.setVideoURI(null); // Очистить содержимое VideoView
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            startButton.setEnabled(false);
        }

    }

    private void stopPlay() { // Остановка воспроизведения аудио
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
}
package com.rpomp.labrab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;


public class CameraActivity extends AppCompatActivity {

    private DrawingView drawingView;
    Camera camera;
    MediaRecorder mediaRecorder;

    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        drawingView = new DrawingView(this);
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        frameLayout.addView(drawingView);

        File pictures = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        photoFile = new File(pictures, "myphoto112.jpg");

        SurfaceHolder holder = drawingView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

        Button captureButton = findViewById(R.id.btn_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPicture(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        if (camera != null)
            camera.release();
        camera = null;
    }

    public void onClickPicture(View view) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //try {
                //    FileOutputStream fos = new FileOutputStream(photoFile);
                //   fos.write(data);
                //    fos.close();
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}

                Bitmap photoBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                drawingView.setPhotoBitmap(photoBitmap);
            }
        });

    }

    private boolean prepareVideoRecorder() {

        camera.unlock();

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setPreviewDisplay(drawingView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }
    }

    private class DrawingView extends SurfaceView {

        private float previousX;
        private float previousY;
        private float currentX;
        private float currentY;
        private Paint paint;
        private Bitmap photoBitmap;

        public DrawingView(Context context) {
            super(context);
            setBackgroundColor(Color.GRAY);

            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(30);
        }

        public void setPhotoBitmap(Bitmap bitmap) {
            photoBitmap = bitmap;
            invalidate(); // Redraw the view with the new image
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (photoBitmap != null) {
                // Draw the photoBitmap on the canvas at (0, 0)
                canvas.drawBitmap(photoBitmap, 0, 0, null);
            }

            canvas.drawLine(previousX, previousY, currentX, currentY, paint);
            canvas.drawPoint(previousX, previousY, paint);
            canvas.drawPoint(currentX, currentY, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            currentX = event.getX();
            currentY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    previousX = currentX;
                    previousY = currentY;
                    break;
                case MotionEvent.ACTION_UP:
                    currentX = event.getX();
                    currentY = event.getY();
                    invalidate(); // Перерисовываем представление после рисования линии
                    break;
            }

            previousX = currentX;
            previousY = currentY;

            return true;
        }
    }
}
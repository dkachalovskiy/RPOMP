package com.rpomp.labrab6;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GesturesActivity extends AppCompatActivity implements GlassGestureDetector.OnGestureListener {
    private GlassGestureDetector glassGestureDetector;
    private TextView label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);

        label = findViewById(R.id.textView);
        glassGestureDetector = new GlassGestureDetector(this, this);
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:
                // Response for TAP gesture
                label.setText("Нажатие");
                return true;
            case DOUBLE_TAP:
                // Response for TAP gesture
                label.setText("Двойное нажатие");
                return true;
            case SWIPE_FORWARD:
                // Response for SWIPE_FORWARD gesture
                label.setText("Свайп влево");
                return true;
            case SWIPE_BACKWARD:
                // Response for SWIPE_BACKWARD gesture
                label.setText("Свайп вправо");
                return true;
            case SWIPE_UP:
                label.setText("Свайп вверх");
                return true;
            case SWIPE_DOWN:
                label.setText("Свайп вниз");
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (glassGestureDetector.onTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}

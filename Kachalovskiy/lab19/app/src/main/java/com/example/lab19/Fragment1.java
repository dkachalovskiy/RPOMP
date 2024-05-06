package com.example.lab19;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import static android.content.Context.SENSOR_SERVICE;

import java.util.List;

public class Fragment1 extends Fragment {

    private TextView txtView;
    private SensorManager sensorManager;
    private List<Sensor> sensors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);

        txtView = rootView.findViewById(R.id.tvText);
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder sb = new StringBuilder();
        for (Sensor sensor : sensors) {
            sb.append("name = ").append(sensor.getName())
                    .append(", type = ").append(sensor.getType())
                    .append("\nvendor = ").append(sensor.getVendor())
                    .append(" ,version = ").append(sensor.getVersion())
                    .append("\nmax = ").append(sensor.getMaximumRange())
                    .append(", resolution = ").append(sensor.getResolution())
                    .append("\n--------------------------------------\n");
        }
        txtView.setText(sb);

        return rootView;
    }
}

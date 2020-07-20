package com.example.sensorsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private Sensor mProximitySensor;

    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        mTextSensorLight = findViewById(R.id.label_light);
        mTextSensorProximity = findViewById(R.id.label_proximity);

        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        String sensor_error = getResources().getString(R.string.error_no_sensor);
        if (mLightSensor == null) {
            mTextSensorLight.setText(sensor_error);
        }
        if (mProximitySensor == null) {
            mTextSensorProximity.setText(sensor_error);
        }
    }
}
package com.example.sensorsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private Sensor mProximitySensor;

    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private ImageView mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mTextSensorLight = findViewById(R.id.label_light);
        mTextSensorProximity = findViewById(R.id.label_proximity);
        mIcon = findViewById(R.id.icon);

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

    @Override
    protected void onStart() {
        super.onStart();
        if (mLightSensor != null) {
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mProximitySensor != null) {
            mSensorManager.registerListener(this, mProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float sensorValue = sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(getResources().getString(R.string.label_light,
                        sensorValue));
                float maxValue = mLightSensor.getMaximumRange();
                int newValue = (int) (255f * sensorValue / maxValue);
                getWindow().getDecorView().setBackgroundColor(Color.rgb(newValue, newValue, newValue));
                break;
            case Sensor.TYPE_PROXIMITY:


                mIcon.getLayoutParams().width = (int)sensorValue * 25;
                mTextSensorProximity.setText(getResources().getString(R.string.label_proximity,
                        sensorValue));
            default:
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
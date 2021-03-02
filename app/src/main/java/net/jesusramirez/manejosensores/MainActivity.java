package net.jesusramirez.manejosensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mLight;
    private Sensor mSensor;
    List<Sensor> deviceSensors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensor=null;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        deviceSensors= sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor x: deviceSensors) {
            Log.d("Sensor", "Tiene: " + x.getName());
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            List<Sensor> gravSensors = sensorManager.getSensorList(Sensor.TYPE_GRAVITY);
            for(int i=0; i<gravSensors.size(); i++) {
                if ((gravSensors.get(i).getVendor().contains("Google LLC")) &&
                        (gravSensors.get(i).getVersion() == 3)){
                    mSensor = gravSensors.get(i);
                }
            }
        }
        if (mSensor == null){
            // Use the accelerometer.
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
                mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                Log.d("S","Tiene acelerometro");
            } else{
                // Sorry, there are no accelerometers on your device.
                // You can't play this game.
                Log.d("N","No tiene acelerometro");
            }
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            // Success! There's a pressure sensor.
            Log.d("S","Tiene sensor de presion");
        } else {
            // Failure! No pressure sensor.
            Log.d("N","No tiene sensor de presion");
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float lux = event.values[0];
        Log.d("Nose","Dato:" + lux);
        // Do something with this sensor value.
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
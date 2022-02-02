package com.example.womensafety;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.List;

public class AccelerometerManager {

    private static Context aContext = null;


    /**
     * Accuracy configuration
     */
    private static float threshold = 15.0f;
    private static int interval = 200;

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private static long mShakeTimestamp;
    private static int mShakeCount;

    private static Sensor sensor;
    private static SensorManager sensorManager;
    // you could use an OrientationListener array instead
    // if you plans to use more than one listener
    private static AccelerometerListener mListener;

    /**
     * indicates whether or not Accelerometer Sensor is supported
     */
    private static Boolean supported;
    /**
     * indicates whether or not Accelerometer Sensor is running
     */
    private static boolean running = false;

    /**
     * Returns true if the manager is listening to orientation changes
     */
    public static boolean isListening() {
        return running;
    }

    /**
     * Unregisters listeners
     */
    public static void stopListening() {
        running = false;
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Returns true if at least one Accelerometer sensor is available
     */
    public static boolean isSupported(Context context) {
        aContext = context;
        if (supported == null) {
            if (aContext != null) {
                sensorManager = (SensorManager) aContext.
                        getSystemService(Context.SENSOR_SERVICE);

                // Get all sensors in device
                List<Sensor> sensors = sensorManager.getSensorList(
                        Sensor.TYPE_ACCELEROMETER);

                supported = new Boolean(sensors.size() > 0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }

    /**
     * Configure the listener for shaking
     *
     * @param threshold minimum acceleration variation for considering shaking
     * @param interval  minimum interval between to shake events
     */
    public static void configure(int threshold, int interval) {
        AccelerometerManager.threshold = threshold;
        AccelerometerManager.interval = interval;
    }

    /**
     * Registers a listener and start listening
     *
     * @param accelerometerListener callback for accelerometer events
     */
    public static void startListening(AccelerometerListener accelerometerListener) {

        sensorManager = (SensorManager) aContext.
                getSystemService(Context.SENSOR_SERVICE);

        // Take all sensors in device
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER);

        if (sensors.size() > 0) {

            sensor = sensors.get(0);

            // Register Accelerometer Listener
            running = sensorManager.registerListener(
                    sensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_GAME);

            mListener = accelerometerListener;
        }


    }

    /**
     * Configures threshold and interval
     * And registers a listener and start listening
     *
     * @param accelerometerListener callback for accelerometer events
     * @param threshold             minimum acceleration variation for considering shaking
     * @param interval              minimum interval between to shake events
     */
    public static void startListening(
            AccelerometerListener accelerometerListener,
            int threshold, int interval) {
        configure(threshold, interval);
        startListening(accelerometerListener);
    }

    /**
     * The listener that listen to events from the accelerometer listener
     */
    private static final SensorEventListener sensorEventListener =
            new SensorEventListener() {
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }

                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (mListener != null) {
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];

                        float gX = x / SensorManager.GRAVITY_EARTH;
                        float gY = y / SensorManager.GRAVITY_EARTH;
                        float gZ = z / SensorManager.GRAVITY_EARTH;

                        // gForce will be close to 1 when there is no movement.
                        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                            final long now = System.currentTimeMillis();
                            // ignore shake events too close to each other (500ms)
                            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                                return;
                            }

                            // reset the shake count after 3 seconds of no shakes
                            if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                                mShakeCount = 0;
                            }

                            mShakeTimestamp = now;
                            mShakeCount++;

                            mListener.onShake(mShakeCount);
                        }
                    }
                }
            };
}
package com.example.womensafety;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SuppressLint("HandlerLeak")


public class BgService extends Service implements AccelerometerListener {

    String str_address;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    long lastShake = System.currentTimeMillis();
    long SHAKE_THRESHOLD = 5000;

    // Handler that receives messages from the thread.
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            // REPLACE THIS CODE WITH YOUR APP CODE
            // Wait before Toasting Service Message  
            // to give the Service Started message time to display.

            // Toast Service Message.
	/*  		Context context = getApplicationContext();
			CharSequence text = "Service Message";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
	*/

            // Service can stop itself using the stopSelf() method.
            // Not using in this app.  Example statement shown below.
            //stopSelf(msg.arg1);
        }
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }

        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();

        mServiceHandler = new ServiceHandler(mServiceLooper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get message from message pool using handler.
        Message msg = mServiceHandler.obtainMessage();

        // Set start ID (unique to the specific start) in message.
        msg.arg1 = startId;

        // Send message to start job.
        mServiceHandler.sendMessage(msg);

        // Toast Service Started message.
        //	Context context = getApplicationContext();
		
		
		
		
	/*	CharSequence text = "Service Started";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();	
    */

        // Start a sticky.
        return START_STICKY;
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub
//        Toast.makeText(getApplicationContext(), "On Change" + Float.toString(x), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShake(float force) {
        GPSTracker gps;
        gps = new GPSTracker(BgService.this);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            SQLiteDatabase db;
            db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM details", null);
            Cursor c1 = db.rawQuery("SELECT * FROM SOURCE", null);

            SmsManager smsManager = SmsManager.getDefault();

            String source_ph_number = "";

            while (c1.moveToNext()) {
                source_ph_number = c1.getString(0);
            }

            str_address = "https://www.google.com/maps/search/?api=1&query=" + latitude + "%2C" + longitude;

            while (c.moveToNext()) {
                String target_ph_number = c.getString(1);
                smsManager.sendTextMessage(target_ph_number, source_ph_number, "Please help me. I need help immediately. This is where I am now: " + str_address, null, null);
            }
            db.close();

            Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();

//            RGeocoder RGeocoder = new RGeocoder();
//            RGeocoder.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());

//            Toast.makeText(getApplicationContext(), "onShake " + Double.toString(latitude) + " " + Double.toString(longitude), Toast.LENGTH_SHORT).show();
        } else {
            gps.showSettingsAlert();
        }
    }


    // onDestroy method.   Display toast that service has stopped.
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Toast Service Stopped.
        Context context = getApplicationContext();

        Log.i("Sensor", "Service  distroy");

        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }

        CharSequence text = "Women Safety App Service Stopped";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


    }


}
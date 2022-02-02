package com.example.womensafety;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.BgService;
import com.example.womensafety.MainActivity;
import com.example.womensafety.R;

public class DisplayActivity extends Activity {
    Cursor c, c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Button emergencyBtn = findViewById(R.id.emergencyCall);

        TextView display = (TextView) findViewById(R.id.myPhoneDisp);

        emergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                callIntent.setData(Uri.parse("tel://999"));
                startActivity(callIntent);
            }
        });

        SQLiteDatabase db;
        db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);

        c = db.rawQuery("SELECT * FROM details", null);
        c1 = db.rawQuery("SELECT * FROM source", null);

        String phone = "";

        while (c1.moveToNext()) {
            phone = c1.getString(0);
        }

        phone = "My Phone " + phone;

        Log.i("SOURCE_PHONE", String.valueOf(phone));

        display.setText(phone);

        if (c.getCount() == 0) {
            showMessage("Error", "No records found.");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("Name: " + c.getString(0) + "\n");
            buffer.append("Number: " + c.getString(1) + "\n");
        }

        showMessage("Details", buffer.toString());
        Intent i_startservice = new Intent(DisplayActivity.this, BgService.class);
        startService(i_startservice);
        Toast.makeText(getApplicationContext(), "Bg Service started", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void showMessage(String title, String message) {
    }


    public void back(View v) {
        Intent i_back = new Intent(DisplayActivity.this, MainActivity.class);
        startActivity(i_back);
    }
}
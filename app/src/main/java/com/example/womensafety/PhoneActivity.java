package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        final Button button = findViewById(R.id.saveSourcePhone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_no(view);
            }
        });

        final Button bckBtn = findViewById(R.id.backBtn);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(view);
            }
        });
    }

    public void verify_no(View v) {
        EditText source_no = this.findViewById(R.id.phoneNotxt);
        String str_source_no = source_no.getText().toString();

        Log.d("PHONE_SAVE", "Verify No " + str_source_no);

        SQLiteDatabase db;
        db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS source(number VARCHAR);");
        db.execSQL("INSERT INTO source VALUES('" + str_source_no + "');");
        Toast.makeText(getApplicationContext(), str_source_no + " Successfully Saved", Toast.LENGTH_SHORT).show();
        db.close();
        back(v);
    }

    public void back(View v) {
        Intent i_back = new Intent(PhoneActivity.this, MainActivity.class);
        startActivity(i_back);
    }
}
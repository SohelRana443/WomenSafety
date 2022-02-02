package com.example.womensafety;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Instructions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
	}
	
	public void back(View v) {
		Intent i_back=new Intent(Instructions.this,MainActivity.class);
		startActivity(i_back);
	}
}

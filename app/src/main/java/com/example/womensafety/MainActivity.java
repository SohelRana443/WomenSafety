package com.example.womensafety;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.womensafety.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        binding.instructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instruct();
            }
        });

        binding.registerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPhone();
            }
        });

        binding.viewRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display();
            }
        });
    }

    private void registerPhone() {
        Intent i_registerphone = new Intent(MainActivity.this, PhoneActivity.class);
        startActivity(i_registerphone);
    }

    public void register() {
        Intent i_register = new Intent(MainActivity.this, Register.class);
        startActivity(i_register);
    }

    public void display() {
        Intent i_display = new Intent(MainActivity.this, DisplayActivity.class);
        startActivity(i_display);
    }

    public void instruct() {
        Intent i_help = new Intent(MainActivity.this, Instructions.class);
        startActivity(i_help);
    }
}
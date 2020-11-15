package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {
    Button select_bt, create_bt;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        select_bt = findViewById(R.id.select_btn);
        select_bt.setOnClickListener(v -> {
            Intent goToProfile = new Intent(HomeActivity.this, ProfileListActivity.class);
            startActivityForResult(goToProfile, 10);
        });

        create_bt = findViewById(R.id.create_btn);
        create_bt.setOnClickListener(v -> {
            Intent goToProfile = new Intent(HomeActivity.this, ProfileCreateActivity.class);
            startActivityForResult(goToProfile, 10);
        });

    }

//    protected void onPause(String stringToSave) {
//        super.onPause();
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putString("reservedPhone", stringToSave);
//        edit.commit();
//    }
}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {
    EditText et;
    Button bt;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        et = findViewById(R.id.editPhone);

        //Need to be exchanged with a user profile data
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("reservedPhone", "");

        et.setText(savedString);

        bt = findViewById(R.id.login_btn);
        bt.setOnClickListener(v -> {
            onPause(et.getText().toString());
            Intent goToProfile = new Intent(ProfileActivity.this, MainActivity.class);
            goToProfile.putExtra("PHONE", et.getText().toString());
            startActivityForResult(goToProfile, 10);
        });
    }

    protected void onPause(String stringToSave) {
        super.onPause();
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("reservedPhone", stringToSave);
        edit.commit();
    }
}
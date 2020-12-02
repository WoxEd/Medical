package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileCreateActivity extends AppCompatActivity {

    private EditText et_first, et_last;
    private Button bt_create2;
    private SharedPreferences prefs = null;

    private DatabaseOpener opener;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);

        opener = new DatabaseOpener(this);
        db = opener.getWritableDatabase();

        et_first = findViewById(R.id.create_editText_1);
        et_last = findViewById(R.id.create_editText_2);

        prefs = getSharedPreferences("profileData", Context.MODE_PRIVATE);

        String first = prefs.getString("first","");
        String last = prefs.getString("last","");
        et_first.setText(first);
        et_last.setText(last);

        bt_create2 = findViewById(R.id.create2_btn);
        bt_create2.setOnClickListener(v -> {
            String firstName = et_first.getText().toString();
            String lastName = et_last.getText().toString();

            if (firstName.trim().isEmpty()) {
                Toast.makeText(this, "First Name should be entered", Toast.LENGTH_LONG).show();
            } else if (lastName.trim().isEmpty()) {
                Toast.makeText(this, "Last Name should be entered", Toast.LENGTH_LONG).show();
            } else {

                opener.insert(db, firstName, lastName);
                startActivity(new Intent(ProfileCreateActivity.this, HomeActivity.class));
                Toast.makeText(this, "Profile Created", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
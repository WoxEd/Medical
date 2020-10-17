package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataActivity extends AppCompatActivity {
    DBHelper myDataBase;
    EditText firstName,lastName,phoneNumber;
    Button savaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        myDataBase = new DBHelper(this);
        firstName=(EditText)findViewById(R.id.editFirstName);
        lastName=(EditText)findViewById(R.id.editLastName);
        phoneNumber=(EditText)findViewById(R.id.editPhone);
        savaButton=(Button)findViewById(R.id.button2);
        addData();

    }
    //Add new Patiant
    public void addData(){
        savaButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted  = myDataBase.insertPatient(firstName.getText().toString(),
                                lastName.getText().toString(),
                                phoneNumber.getText().toString());


                        if(isInserted)
                            Toast.makeText(DataActivity.this,"New Patient inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(DataActivity.this,"Data failed",Toast.LENGTH_LONG).show();

                    }
                }
        );
    }


}
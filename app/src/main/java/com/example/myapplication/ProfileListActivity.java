package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class ProfileListActivity extends AppCompatActivity {

    /**
     * The stored profiles list (NEED TO BE UPDATED W/ DATABASE)
     */
    public static final String SELECTED_FIRST = "FIRST";
    public static final String SELECTED_LAST = "LAST";

    ListView listView;
    Bundle dataToPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        listView = findViewById(R.id.listView);

        listView.setOnItemClickListener((list, view, position,id) -> {
            dataToPass = new Bundle();
            /**
             * ProfileList required using database
             */
//            ProfileList profileList = (ProfileList) listView.getAdapter().getItem(position);
//            dataToPass.putString(SELECTED_FIRST, pList.getFirst());
//            dataToPass.putString(SELECTED_LAST, pList.getLast());
//            ...

            Intent nextActivity = new Intent(ProfileListActivity.this, MainActivity.class);
            nextActivity.putExtras(dataToPass);
            startActivity(nextActivity);

        });
    }
}
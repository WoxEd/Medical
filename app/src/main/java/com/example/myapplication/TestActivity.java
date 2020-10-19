package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * THIS ACTIVITY IS ONLY FOR TESTING DATABASE. IT HAS NO USE IN THE PROJECT
 */
public class TestActivity extends AppCompatActivity {


    private PrototypeOneDBOpener opener;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        opener = new PrototypeOneDBOpener(this);
        db = opener.getWritableDatabase();
        //opener.onUpgrade(db,1,1);
        //opener.onCreate(db);
        testAdd();
        testLoad();

    }


    private void testAdd() {
        /*
        ContentValues entry = new ContentValues();
        entry.put(PrototypeOneDBOpener.COL_DISABILITY, MainActivity.VISION);
        entry.put(PrototypeOneDBOpener.COL_RATING, 10);
        entry.put(PrototypeOneDBOpener.COL_DATE, "10/10/2020");

        db.insert(PrototypeOneDBOpener.TABLE_NAME,null, entry);

         */
        opener.insert(db,MainActivity.VISION,1, "18/10/2020");
    }


    private void testLoad() {
        String[] columns ={PrototypeOneDBOpener.COL_ID, PrototypeOneDBOpener.COL_DISABILITY,
                PrototypeOneDBOpener.COL_RATING, PrototypeOneDBOpener.COL_DATE};

        Cursor results = db.query(false, PrototypeOneDBOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        int idIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_ID);
        int disabilityIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_DISABILITY);
        int ratingIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_RATING);
        int dateIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_DATE);

        while(results.moveToNext()) {
            long id = results.getLong(idIndex);
            String disability = results.getString(disabilityIndex);
            int rating = results.getInt(ratingIndex);
            String date = results.getString(dateIndex);

            String formatted = String.format("The id is: %d, The disability is: %s, The rating is %d, The date is: %s", id, disability, rating, date);
            System.out.println("Database:" +  formatted);
        }

    }
}
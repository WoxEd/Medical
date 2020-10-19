package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {

    /**
     * TextViews to show the selected data
     */
    TextView selectedDisability, selectedSymptom, savedDate;
    /**
     * Buttons to go back to the main page
     */
    Button goToMainBtn;

    /**
     * ArrayList that holds the object which contains all the data
     */
    ArrayList<SummaryObject> list = new ArrayList<>();

    /**
     * Database opener created for the purposes of prototype 1.
     */
    private PrototypeOneDBOpener opener;

    /**
     * DB which holds our entries
     */
    private SQLiteDatabase db;

    /**
     * The arraylist holds labels which are the dates of the data range
     */
    private ArrayList<String> labels;

    /**
     * Fields used for debugging purposes
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        //Opener and db created
        opener = new PrototypeOneDBOpener(this);
        db = opener.getWritableDatabase();
        //Method that adds entries to database
        opener.reset(db);
        testAdd();
        //Entries from db loaded and saved to ArrayList
        loadEntries();
        //Labels created which is the past 7 days
        labels = createLabels();

        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = createBarEntries();
        BarDataSet bardataset = new BarDataSet(entries, "Symptoms Scale");

        BarDataSet b2 = new BarDataSet(createBarEntries(), "New Label");

        ArrayList<String> labels = createLabels();

        BarData data = new BarData(labels, bardataset);

        data.addDataSet(b2);
        // set the data and list of labels into chart
        barChart.setData(data);
        // set the description
        barChart.setDescription("This chart tracks illness symptoms");
//        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        barChart.animateY(5000);
    }


    /**
     * This will load entries for the summary page.
     */
    private void loadEntries() {
        String[] columns = {PrototypeOneDBOpener.COL_ID, PrototypeOneDBOpener.COL_DISABILITY,
                PrototypeOneDBOpener.COL_RATING, PrototypeOneDBOpener.COL_DATE};

        Cursor results = db.query(false, PrototypeOneDBOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        int idIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_ID);
        int disabilityIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_DISABILITY);
        int ratingIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_RATING);
        int dateIndex = results.getColumnIndex(PrototypeOneDBOpener.COL_DATE);

        while (results.moveToNext()) {
            long id = results.getLong(idIndex);
            String disability = results.getString(disabilityIndex);
            int rating = results.getInt(ratingIndex);
            String date = results.getString(dateIndex);
            Log.d("LOAD", "LOADED " + disability + " " + rating + " " + date);
            list.add(new SummaryObject(disability, rating, date));
        }
    }

    /**
     * Labels are generated from the current date to the past 7 days
     * TODO: Modify this method to generate summary over a 7 day timespan rather than just the last 7 days
     */
    private ArrayList<String> createLabels() {

        ArrayList<String> labels = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        //initializes the first day of the calendar to be 6 days before the current day
        cal.add(Calendar.DAY_OF_YEAR, -6);

        for(int i = 0; i<7; i++) {
            //adds a day to each calendar, for a total of 6 days
            cal.add(Calendar.DAY_OF_YEAR, 1);
            Log.d("createLabels()", formatter.format(cal.getTime()));
            labels.add(formatter.format(cal.getTime()));
        }
        return labels;
    }

    private ArrayList<BarEntry> createBarEntries() {

        ArrayList<BarEntry> entry = new ArrayList<>();

        for( SummaryObject obj : list ) {
            String disability = obj.getDisabilityType();
            int severity = obj.getRating();
            String date = obj.getDate();

            Log.d("createBarEntries()", ("Entry date = " + date));
            //This line of code should only run if the date is within the last 7 days
            if(labels.contains(date)) {
                entry.add(createEntry(disability, severity, date));
            }
        }
        return entry;
    }

    /**
     * Creates a bar entry
     * BarEntry( values example 10f will have 10 height, index example index 0 will be the first bar in our example index [0,6] is acceptable
     */
    private BarEntry createEntry(String disability, int severity, String date) {
        int index = getIndex(date);
        BarEntry entry = new BarEntry(severity, index);
        return entry;
    }

    /**
     * Converts the date of an entry to an entry on the bar graph.
     */
    private int getIndex(String date) {
        for(int i=0; i<labels.size(); i++) {
            Log.d("getIndex()", "Comparing dates " + date + " and " + labels.get(i));
            if(date.equals(labels.get(i))) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Temporary method used to insert data to test the entries
     */
    private void testAdd() {
        //inserts entry with values (vision, 5, 10/18/2020)
        opener.insert(db, MainActivity.VISION,1, "18/10/2020");
        opener.insert(db, MainActivity.VISION,7, "17/10/2020");
        opener.insert(db, MainActivity.VISION,4, "16/10/2020");
        opener.insert(db, MainActivity.VISION,2, "15/10/2020");
        opener.insert(db, MainActivity.VISION,10, "14/10/2020");
        opener.insert(db, MainActivity.VISION,3, "13/10/2020");
        opener.insert(db, MainActivity.VISION,4, "12/10/2020");
        opener.insert(db, MainActivity.VISION,2, "11/10/2020");
        opener.insert(db, MainActivity.VISION,3, "10/10/2020");
        opener.insert(db, MainActivity.VISION,1, "09/10/2020");

        opener.insert(db, MainActivity.SPEAKING,3, "18/10/2020");
        opener.insert(db, MainActivity.HEARING,2, "17/10/2020");
        opener.insert(db, MainActivity.ELIMINATING,6, "16/10/2020");
        opener.insert(db, MainActivity.MENTAL,8, "15/10/2020");
        opener.insert(db, MainActivity.MENTAL,9, "14/10/2020");
        opener.insert(db, MainActivity.WALKING,4, "13/10/2020");
        opener.insert(db, MainActivity.FEEDING,3, "12/10/2020");
        opener.insert(db, MainActivity.FEEDING,5, "11/10/2020");
    }

}
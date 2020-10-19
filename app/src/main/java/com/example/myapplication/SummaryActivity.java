package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;import java.text.SimpleDateFormat;
import java.util.Calendar;

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
     * Fields used for storing index of the subsets of data
     */
    private static final int VISION_INDEX = 0;
    private static final int SPEAKING_INDEX = 1;
    private static final int HEARING_INDEX = 2;
    private static final int WALKING_INDEX = 3;
    private static final int ELIMINATING_INDEX = 4;
    private static final int FEEDING_INDEX = 5;
    private static final int DRESSING_INDEX = 6;
    private static final int MENTAL_INDEX = 7;

    /**
     * Fields used for storing the colour of data subsets
     */
    private static final int VISION_COLOUR = Color.RED;
    private static final int SPEAKING_COLOUR = Color.YELLOW;
    private static final int HEARING_COLOUR = Color.MAGENTA;
    private static final int WALKING_COLOUR = Color.BLACK;
    private static final int ELIMINATING_COLOUR = Color.LTGRAY;
    private static final int FEEDING_COLOUR = Color.BLUE;
    private static final int DRESSING_COLOUR = Color.GREEN;
    private static final int MENTAL_COLOUR = Color.CYAN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        //Opener and db created
        opener = new PrototypeOneDBOpener(this);
        db = opener.getWritableDatabase();

        //For demonstration purposes
        opener.reset(db);
        testAdd();

        //Entries from db loaded and saved to ArrayList
        loadEntries();

        //Labels created which is the past 7 days
        labels = createLabels();

        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);


        // set the data and list of labels into chart
        BarData data = new BarData(labels);
        ArrayList<BarDataSet> barDataSets = getDataSets();
        for(BarDataSet dataSet : barDataSets) {
            if(dataSet.getEntryCount() > 0) //we only add data that's not empty to avoid white space
            data.addDataSet(dataSet);
        }

        barChart.setData(data);
        // set the description
        barChart.setDescription("Summary of the past 7 days");
        barChart.animateY(1000);
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

    /**
     * Loops through the list of entries creating a BarDataSet for each disability type
     */
    private ArrayList<BarDataSet> getDataSets() {
        ArrayList<BarDataSet> data = new ArrayList<>();
        //Inserts a new BarDataSet for each disability type
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.VISION));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.SPEAKING));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.HEARING));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.WALKING));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.ELIMINATING));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.FEEDING));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.DRESSING));
        data.add(new BarDataSet(new ArrayList<BarEntry>(),MainActivity.MENTAL));

        //Sets the colours of each disability type
        data.get(VISION_INDEX).setColor(VISION_COLOUR);
        data.get(SPEAKING_INDEX).setColor(SPEAKING_COLOUR);
        data.get(HEARING_INDEX).setColor(HEARING_COLOUR);
        data.get(WALKING_INDEX).setColor(WALKING_COLOUR);
        data.get(ELIMINATING_INDEX).setColor(ELIMINATING_COLOUR);
        data.get(FEEDING_INDEX).setColor(FEEDING_COLOUR);
        data.get(DRESSING_INDEX).setColor(DRESSING_COLOUR);
        data.get(MENTAL_INDEX).setColor(MENTAL_COLOUR);

        for(SummaryObject obj : list) {

            String disabilityType = obj.getDisabilityType();
            int rating = obj.getRating();
            String date = obj.getDate();

            switch(disabilityType) {
                case MainActivity.VISION:
                    data.get(VISION_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.SPEAKING:
                    data.get(SPEAKING_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.HEARING:
                    data.get(HEARING_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.WALKING:
                    data.get(WALKING_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.ELIMINATING:
                    data.get(ELIMINATING_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.FEEDING:
                    data.get(FEEDING_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.DRESSING:
                    data.get(DRESSING_INDEX).addEntry(createEntry(rating,date));
                    break;
                case MainActivity.MENTAL:
                    data.get(MENTAL_INDEX).addEntry(createEntry(rating,date));
                    break;
            }
        }

        return data;
    }

    /**
     * Creates a bar entry
     * BarEntry( values example 10f will have 10 height, index example index 0 will be the first bar in our example index [0,6] is acceptable
     */
    private BarEntry createEntry( int severity, String date) {
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
        opener.insert(db, MainActivity.SPEAKING,1, "18/10/2020");
        opener.insert(db, MainActivity.VISION,6, "18/10/2020");
        opener.insert(db, MainActivity.ELIMINATING,1, "18/10/2020");
        opener.insert(db, MainActivity.ELIMINATING,2, "18/10/2020");
        opener.insert(db, MainActivity.SPEAKING,3, "18/10/2020");
        opener.insert(db, MainActivity.DRESSING,4, "18/10/2020");
        opener.insert(db, MainActivity.FEEDING,7, "17/10/2020");
        opener.insert(db, MainActivity.VISION,4, "16/10/2020");
        opener.insert(db, MainActivity.VISION,2, "15/10/2020");
        opener.insert(db, MainActivity.VISION,10, "14/10/2020");
        opener.insert(db, MainActivity.SPEAKING,3, "13/10/2020");
        opener.insert(db, MainActivity.VISION,4, "12/10/2020");
        opener.insert(db, MainActivity.VISION,2, "11/10/2020");
        opener.insert(db, MainActivity.VISION,6, "10/10/2020");
        opener.insert(db, MainActivity.SPEAKING,10, "09/10/2020");
    }

}
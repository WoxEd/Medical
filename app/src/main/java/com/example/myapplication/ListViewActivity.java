package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class ListViewActivity extends AppCompatActivity {

    /**
     * Database opener created for the purposes of prototype 1.
     */
    private PrototypeOneDBOpener opener;

    /**
     * DB which holds our entries
     */
    private SQLiteDatabase db;

    /**
     * ArrayList for holding all SummaryObjects that will populate the ListView
     */
    private ArrayList<SummaryObject> list = new ArrayList<>();

    /**
     * ListView which holds the saved entries
     */
    private ListView savedList;
    private TextView date;
    private TextView disability;
    private TextView severity;

    /**
     * Used for reversing sort order
     */
    boolean reverseDate = false;
    boolean reverseDisability = false;
    boolean reverseSeverity = false;

    private Button filter;
    private Button showSummary;

    private View filterButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        filter = findViewById(R.id.listFilterButton);
        showSummary = findViewById(R.id.viewSummaryButton);

        loadEntries();
        sortDate(true);
        inflateList();
        addSortOnClickListeners();


        createFilterMenu();
        createFilterMenuActions();


        showSummary.setOnClickListener(e -> {
            Intent goToSummary = new Intent(ListViewActivity.this, SummaryActivity.class);
            goToSummary.putExtra(MainActivity.LIST, list);
            startActivity(goToSummary);
        });
    }

    private void createFilterMenu() {
        filterButtons = getLayoutInflater().inflate(R.layout.list_filter_layout,null);

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(filterButtons);

        AlertDialog dialog = builder.create();
        filter.setOnClickListener( e-> {
            dialog.show();
        });
    }

    private void createFilterMenuActions() {
        Button clear = filterButtons.findViewById(R.id.emptyDatabase);
        Button add = filterButtons.findViewById(R.id.addDatabase);
        Button reset = filterButtons.findViewById(R.id.resetDatabase);

        clear.setOnClickListener( e -> {
            opener.reset(db);
            loadEntries();
            inflateList();
        });
        add.setOnClickListener( e -> {
            opener.testAdd(db);
            loadEntries();
            inflateList();});
        reset.setOnClickListener( e -> {
            opener.dropAdd(db);
            loadEntries();
            inflateList();
            inflateList();});


        Button seven = filterButtons.findViewById(R.id.weekSummary);
        Button month = filterButtons.findViewById(R.id.monthSummary);
        Button year = filterButtons.findViewById(R.id.yearSummary);
        Button all = filterButtons.findViewById(R.id.allSummary);


        seven.setOnClickListener(e -> {selectWeek();});
        month.setOnClickListener(e -> {selectYearWithMonth(true);});
        year.setOnClickListener(e -> {selectYearWithMonth(false);});

        all.setOnClickListener(e -> {
            //goToAll
        });
    }


    public void selectWeek() {
        DatePickerDialog picker;
        Calendar calendar = Calendar.getInstance();
        // Get the current day, month, year
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Create a calendar dialog for user to select the date, today's date is the default
        picker = new DatePickerDialog(ListViewActivity.this, (datePicker, calYear, calMonth, calDate) -> {
            String formattedDate = createDateString(calYear,calMonth+1,calDate);//String.format(DATE_FORMAT, calYear, calMonth, calDate);
            Log.d("Week summary start date", formattedDate );
        }, year, month, day);
        picker.setMessage("Select the ending day of week");
        picker.show();
    }

    public void selectYearWithMonth(boolean selectMonth) {
        View numberPicker = getLayoutInflater().inflate(R.layout.number_picker,null);
        NumberPicker monthPicker = numberPicker.findViewById(R.id.monthPicker);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month+1);
        if(!selectMonth)
            monthPicker.setEnabled(false);


        NumberPicker yearPicker = numberPicker.findViewById(R.id.yearPicker);
        yearPicker.setMinValue(2019);
        yearPicker.setMaxValue(2099);
        yearPicker.setValue(year);


        String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthPicker.setDisplayedValues(months);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.submit_option, (dialog,id)->{
            Log.d("Number Picker Value: ", yearPicker.getValue()+ " " + monthPicker.getValue());
        });
        builder.setNegativeButton(R.string.cancel_option, (dialog,id)->{});

        builder.setView(numberPicker);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Generates an array of 3 numbers containing year month and day
     * @param date The String format for a date (ie 2020-01-10)
     * @return An array of integers containing 3 elements in order of, year, month, day
     */
    public static int[] getDates(String date) {
        int dates[] = new int[3];

        //2020-10-10 - format
        //0123456789 - index

        //year
        dates[0] = Integer.parseInt(date.substring(0,4));
        //month
        dates[1] = Integer.parseInt(date.substring(5,7));
        //day
        dates[2] = Integer.parseInt(date.substring(8,10));

        return dates;
    }

    /**
     * Creates a string for a date using the format YYYY-MM-DD
     * @param year The integer value representing year (ie: 2002)
     * @param month The integer value representing month (1-12)
     * @param day The the integer value representing day (1-31)
     * @return String format of the integer values (ie: 2002-01-31)
     */
    public static String createDateString(int year, int month, int day) {
        String date = "";

        date += year;
        date += "-";

        if(month < 10)
            date += "0";
        date += (month);
        date += "-";

        if(day < 10)
            date += "0";
        date += day;

        return date;
    }


    private void addSortOnClickListeners() {
        date = findViewById(R.id.entryDate);
        date.setOnClickListener(e -> {
            Toast.makeText(this, "Sort by date", Toast.LENGTH_SHORT).show();
            sortDate(reverseDate);
            reverseDate = !reverseDate;

        });
        disability = findViewById(R.id.entryType);
        disability.setOnClickListener(e -> {
            Toast.makeText(this, "Sort by disability", Toast.LENGTH_SHORT).show();
            sortDisability(reverseDisability);
            reverseDisability = !reverseDisability;
        });

        severity = findViewById(R.id.entryRating);
        severity.setOnClickListener(e -> {
            Toast.makeText(this, "Sort by severity", Toast.LENGTH_SHORT).show();
            reverseSeverity = !reverseSeverity;
            sortSeverity(reverseSeverity);
        });
    }

    /**
     * Loads entries from DB into ArrayList
     */
    private void loadEntries() {
        list = new ArrayList<>();
        opener = new PrototypeOneDBOpener(this);
        db = opener.getWritableDatabase();
        String[] columns = {PrototypeOneDBOpener.COL_ID, PrototypeOneDBOpener.COL_DISABILITY,
                PrototypeOneDBOpener.COL_RATING, PrototypeOneDBOpener.COL_DATE};
        Cursor results = opener.selectAll(db,null);
        //Cursor results = opener.selectByDate(db,null,"2020-11-05");
        //Cursor results = opener.selectBetween(db,null, "2020-10-26", "2020-11-05");
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
     * Uses a comparator to sort the ArrayList by date.
     * @param reverse True to reverse sort
     */
    public void sortDate(boolean reverse) {
        Comparator<SummaryObject> byDate = (obj1, obj2) -> (reverse) ? -obj1.getDate().compareTo(obj2.getDate()) : obj1.getDate().compareTo(obj2.getDate());
        Collections.sort(list, byDate);
        inflateList();
    }

    /**
     * Uses a comparator to sort the ArrayList by disability name.
     * @param reverse True to reverse sort
     */
    public void sortDisability(boolean reverse) {
        Comparator<SummaryObject> byDisability = (obj1, obj2) -> (reverse) ? -obj1.getDisabilityType().compareTo(obj2.getDisabilityType()) : obj1.getDisabilityType().compareTo(obj2.getDisabilityType());
        Collections.sort(list, byDisability);
        inflateList();
    }

    /**
     * Uses a comparator to sort the ArrayList by severity.
     * @param reverse True to reverse sort
     */
    public void sortSeverity(boolean reverse) {
        Comparator<SummaryObject> bySeverity = (obj1, obj2) -> (reverse) ? -(obj1.getRating() - obj2.getRating()) : obj1.getRating() - obj2.getRating();
        Collections.sort(list, bySeverity);
        inflateList();
    }

    /**
     * Updates the list to display current filters and sorts
     */
    private void inflateList() {
        savedList = findViewById(R.id.savedList);
        MyListAdapter adapter = new MyListAdapter(this,list);
        savedList.setAdapter(adapter);
    }

    /**
     * MyListAdapter is an inner class created to help display ListView
     */
    public class MyListAdapter extends BaseAdapter {

        /**
         * Context for which the list is on
         */
        private Context context;

        /**
         * List of summary objects
         */
        private ArrayList<SummaryObject> list;

        /**
         * Two Arg constructor
         */
        protected MyListAdapter(Context context, ArrayList<SummaryObject> list) {
            setContext(context);
            setList(list);
        }

        /**
         * Returns size of list
         * @return size of list
         */
        @Override
        public int getCount() {
            return getList().size();
        }

        /**
         * Returns item at position in list
         * @param position position of item
         * @return item at position
         */
        @Override
        public Object getItem(int position) {
            return getList().get(position);
        }

        /**
         *
         * @param position
         * @return item id
         */
        @Override
        public long getItemId(int position) {
            return -1;
        }

        /**
         * Inflates the view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view,parent,false);

            TextView disabilityType = convertView.findViewById(R.id.entryType);
            TextView severity = convertView.findViewById(R.id.entryRating);
            TextView date = convertView.findViewById(R.id.entryDate);

            disabilityType.setText(getList().get(position).getDisabilityType());
            severity.setText(Integer.toString(getList().get(position).getRating()));
            date.setText(getList().get(position).getDate());

            return convertView;
        }

        /**
         * Mutator for context
         */
        private void setContext(Context context) { this.context = context; }

        /**
         * Accessor for context
         */
        private Context getContext() { return this.context; }

        /**
         * Mutator for list
         */
        private void setList(ArrayList<SummaryObject> list) { this.list = list; }

        /**
         * Accessor for list
         */
        private ArrayList<SummaryObject> getList() { return this.list; }
    }
}
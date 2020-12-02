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
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ListViewActivity extends AppCompatActivity {

    /**
     * Database opener created for the purposes of prototype 1.
     */
    private DatabaseOpener opener;

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
    private TextView title;

    /**
     * Used for reversing sort order
     */
    boolean reverseDate = false;
    boolean reverseDisability = false;
    boolean reverseSeverity = false;

    /**
     * Filter button on ListView Page
     */
    private Button filter;

    /**
     * Show summary Button on ListView Page
     */
    private Button showSummary;

    /**
     * View which is displayed on an AlertDialog when filter button is pressed
     */
    private View filterButtons;

    /**
     * The cursor for fetching results either all or results between dates
     */
    private Cursor results;

    /**
     * Start date of the results
     */
    private String startDate;

    /**
     * End date of the results
     */
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_list_view);

        opener = new DatabaseOpener(this);
        db = opener.getWritableDatabase();

        title = findViewById(R.id.listTitle);
        setDefaultValues();
        updateList(false);

        filter = findViewById(R.id.listFilterButton);
        showSummary = findViewById(R.id.viewSummaryButton);
        addSortOnClickListeners();
        createFilterMenu();

        showSummary.setOnClickListener(e -> goToSummary());

        savedList.setOnItemClickListener( (parent,view,pos,id) -> displayEntryAndQuestions(list.get(pos)));
    }

    /**
     * Goes to summary activity passing in the current ListView
     */
    private void goToSummary() {
        Intent goToSummary = new Intent(ListViewActivity.this, SummaryActivity.class);
        goToSummary.putExtra(MainActivity.LIST, list);
        startActivity(goToSummary);
    }

    /**
     * In the ListView of entries, when one of them are pressed it will display all questions and notes
     */
    private void displayEntryAndQuestions(SummaryObject summaryObject) {
        Cursor questions = opener.selectAll(db, summaryObject.getId());

        int questionIndex = questions.getColumnIndex(DatabaseOpener.COL_QUESTION);
        int answerIndex= questions.getColumnIndex(DatabaseOpener.COL_ANSWER);

        ArrayList<String> questionResults = new ArrayList<>();
        while(questions.moveToNext()) {
            String question = questions.getString(questionIndex);
            String answer = questions.getString(answerIndex);
            Log.d("Load Questions", question + " " +answer );
            questionResults.add(question);
            questionResults.add(answer);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Information");
        String message = "";
        for(int i =0; i< questionResults.size(); i+=2 ){
            message += questionResults.get(i) + ": " + questionResults.get(i+1) + "\n";
        }
        TextView results = new TextView(this);
        results.setText(message);
        results.setTextSize(20f);
        builder.setView(results);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Creates a default start and end date, updates title, creates cursor
     */
    private void setDefaultValues() {
        Calendar cal = Calendar.getInstance();
        endDate = createDateString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.DAY_OF_YEAR, -6);
        startDate = createDateString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
        updateTitleForTwoDates();
        updateList(false);
    }

    /**
     * Creates a menu when filter is selected on the ListView
     * This adds functionality to select date, month, year, all buttons.
     */
    private void createFilterMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        filterButtons = getLayoutInflater().inflate(R.layout.list_filter_layout,null);
        builder.setView(filterButtons);

        AlertDialog dialog = builder.create();
        filter.setOnClickListener( e-> dialog.show());

        /*DEBUG BUTTONS*/
        //TODO Delete debug buttons
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
        /*DEBUG BUTTONS*/

        Button selectDates = filterButtons.findViewById(R.id.weekSummary);
        Button selectMonth = filterButtons.findViewById(R.id.monthSummary);
        Button selectYear  = filterButtons.findViewById(R.id.yearSummary);
        Button selectAll = filterButtons.findViewById(R.id.allSummary);


        addSelectDateListeners(selectDates);
        selectMonth.setOnClickListener(e -> selectYearWithMonth(true));
        selectYear.setOnClickListener(e -> selectYearWithMonth(false));

        selectAll.setOnClickListener(e -> {
            updateList(true);
            updateTitleForAll();
        });
    }

    /**
     * Adds function to the select dates button. Updates the list when dates are successfully selected
     * @param button Button adding listener to
     */
    public void addSelectDateListeners(Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View selectDates = getLayoutInflater().inflate(R.layout.select_two_dates,null);
        builder.setView(selectDates);
        builder.setPositiveButton(R.string.submit_option, (d,id)->{
            if(startDate == null || endDate == null) {
                Toast.makeText(this, "Both start and end dates must be selected", Toast.LENGTH_SHORT).show();
            } else if(startDate.compareTo(endDate) > 0) {
                Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            } else if(startDate.equals(endDate)) {
                updateTitleForSameDay();
            } else {
                updateTitleForTwoDates();
            }
        });

        AlertDialog dialog = builder.create();
        button.setOnClickListener(e->dialog.show());

        Button startDateButton = selectDates.findViewById(R.id.selectStartDate);
        Button endDateButton = selectDates.findViewById(R.id.selectEndDate);
        TextView startDateText = selectDates.findViewById(R.id.textStartDate);
        TextView endDateText = selectDates.findViewById(R.id.textEndDate);

        startDateButton.setOnClickListener( e-> setDateFromCalendar(true, startDateText));
        endDateButton.setOnClickListener( e-> setDateFromCalendar(false, endDateText));
    }


    /**
     * Selects either a start date or end date from a calendar dialog. Prompted by the select dates button.
     * @param start true if setting a start date. false for end date
     * @param text the TextView to be updated for the date
     */
    private void setDateFromCalendar(boolean start, TextView text) {
        DatePickerDialog picker;
        Calendar calendar = Calendar.getInstance();
        // Get the current day, month, year
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Create a calendar dialog for user to select the date, today's date is the default
        picker = new DatePickerDialog(ListViewActivity.this, (datePicker, calYear, calMonth, calDate) -> {
            String date = createDateString(calYear, calMonth+1, calDate);

            if(start)
                startDate = date;
            else
                endDate = date;

            text.setText(date);


        }, year, month, day);
        picker.setMessage("Select the ending day of week");
        picker.show();
    }

    /**
     * Selects a month/year from the number picker. This is prompted when year or month button is pressed
     * @param selectMonth true if month is to be selected. When it's false the month box is disabled
     */
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

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthPicker.setDisplayedValues(months);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.submit_option, (dialog,id)->{
            int selectedMonth = monthPicker.getValue();
            int selectedYear = yearPicker.getValue();
            Log.d("Number Picker Value: ", selectedMonth+ " " + selectedYear);
            if(selectMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, selectedMonth-1);
                cal.set(Calendar.YEAR, selectedYear);
                startDate = createDateString(selectedYear, selectedMonth, 1);
                endDate = createDateString(selectedYear, selectedMonth, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                updateTitleForMonth();
            } else {
                startDate = createDateString(selectedYear, 1, 1);
                endDate = createDateString(selectedYear, 12, 31);
                updateTitleForYear();
            }
            updateList(false);

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
        int[] dates = new int[3];
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
        String format = "%s-%s-%s";
        String yearText = Integer.toString(year);
        String monthText = (month < 10) ? "0" + month : Integer.toString(month);
        String dateText = (day < 10) ? "0" + day : Integer.toString(day);
        String date = String.format(Locale.CANADA, format, yearText, monthText, dateText);
        return date;
    }


    /**
     * Click listeners for the columns of the list. These allow the list to be sorted on click
     * TODO Change columns to have arrows so user knows about sorting
     */
    private void addSortOnClickListeners() {
        date = findViewById(R.id.entryDate);
        date.setOnClickListener(e -> {
            sortDate(reverseDate);
            reverseDate = !reverseDate;
        });

        disability = findViewById(R.id.entryType);
        disability.setOnClickListener(e -> {
            sortDisability(reverseDisability);
            reverseDisability = !reverseDisability;
        });

        severity = findViewById(R.id.entryRating);
        severity.setOnClickListener(e -> {
            reverseSeverity = !reverseSeverity;
            sortSeverity(reverseSeverity);
        });
    }

    /**
     * Updates the cursor to select between or select all
     * Then loads new select entries and updates the list
     * @param all true if select all
     */
    private void updateList(boolean all) {
        if(all)
            results = opener.selectAllEntry(db, HomeActivity.currentProfileId);
        else
            results =  opener.selectBetween(db, HomeActivity.currentProfileId, startDate, endDate);
        loadEntries();
        inflateList();
    }


    /**
     * Loads entries from DB into ArrayList list
     */
    private void loadEntries() {
        list = new ArrayList<>();
        int idIndex = results.getColumnIndex(DatabaseOpener.COL_ID);
        int disabilityIndex = results.getColumnIndex(DatabaseOpener.COL_DISABILITY);
        int ratingIndex = results.getColumnIndex(DatabaseOpener.COL_RATING);
        int dateIndex = results.getColumnIndex(DatabaseOpener.COL_DATE);

        while (results.moveToNext()) {
            long id = results.getLong(idIndex);
            String disability = results.getString(disabilityIndex);
            int rating = results.getInt(ratingIndex);
            String date = results.getString(dateIndex);
            Log.d("LOAD", "LOADED " + disability + " " + rating + " " + date);
            list.add(new SummaryObject(id, disability, rating, date));
        }
    }

    /**
     * Updates the list to display current filters and sorts
     */
    private void inflateList() {
        savedList = findViewById(R.id.savedList);
        MyListAdapter adapter = new MyListAdapter(this, list);
        savedList.setAdapter(adapter);
    }


    /**
     * Sets the title to be between two selected dates
     */
    private void updateTitleForTwoDates() {
        title.setText(startDate + " to " + endDate);
        updateList(false);
    }

    /**
     * Sets the title to on the selected Month and Year
     */
    private void updateTitleForMonth() {
        int[] dates = getDates(startDate);
        title.setText(new DateFormatSymbols().getMonths()[dates[1]-1] + " " + dates[0]);
        updateList(false);
    }

    /**
     * Sets the title to the selected year
     */
    private void updateTitleForYear() {
        title.setText("Year of " + getDates(startDate)[0]);
        updateList(false);
    }

    /**
     * Sets the title to show that all entries are displayed
     */
    private void updateTitleForAll(){
        title.setText("Showing all entries");
        updateList(true);
    }

    /**
     * Sets the title to show only one day is being shown
     */
    private void updateTitleForSameDay(){
        title.setText(startDate);
        updateList(false);
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
     * MyListAdapter is an inner class created to help display ListView
     */
    public static class MyListAdapter extends BaseAdapter {

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
         *  Override currently does nothing
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
            convertView = inflater.inflate(R.layout.entry_list_design,parent,false);

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
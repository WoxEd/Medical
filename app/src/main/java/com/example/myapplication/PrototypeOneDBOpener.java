package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a database opener. Created to fulfill the needs of Prototype 1.
 * It has a single table which will store the following attributes
 * ID: prime key
 * Disability Type: Example "VISION"
 * Rating (Severity) : A number between 1-10
 * Date: Time of the entry
 */
public class PrototypeOneDBOpener extends SQLiteOpenHelper {

    /**
     * Name of database
     */
    private static final String DATABASE_NAME = "disability.db";

    /**
     * Database version number
     */
    private static final int VERSION_NUMBER = 1;

    /**
     * Name of table
     */
    protected static final String TABLE_NAME = "DISABILITY";

    /**
     * Name of the column containing id
     */
    protected static final String COL_ID = "_id";

    /**
     * Name of column which holds the severity rating 1-10
     */
    protected static final String COL_RATING = "RATING";

    /**
     * Name of column which holds the disability name
     */
    protected static final String COL_DISABILITY = "DISABILITY";

    /**
     * Name of column which holds date
     */
    protected static final String COL_DATE = "ENTRY_DATE";

    /**
     * Create table statement
     */
    private static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " ("+COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_DISABILITY + " text," + COL_RATING + " INTEGER, " + COL_DATE + " text)";

    /**
     * Insert statement
     */
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + " (" + COL_DISABILITY + "," + COL_RATING +"," + COL_DATE+ ")" + " VALUES (\'%s\',\'%d\',\'%s\')";

    /**
     * Constructor required to create database
     */
    public PrototypeOneDBOpener(Context context) {
        super(context,DATABASE_NAME,null,VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_STATEMENT);
        Log.d("DB Helper", CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void insert(SQLiteDatabase sqLiteDatabase, String disabilityType, int severity, String date) {
        String insert = String.format(INSERT_STATEMENT,disabilityType,severity,date);
        Log.d("DB Helper",insert);
        sqLiteDatabase.execSQL(insert);
    }

    public void reset(SQLiteDatabase sqLiteDatabase) {
        Log.d("DB Helper", "Dropping and recreating tables");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_STATEMENT);
    }

    /**
     * Performs a select all statement for a user
     * @param user The name of the profile we wish to preform a select all in
     * @return Cursor object which can be used to increment over the selected elements
     */
    public Cursor selectAll(SQLiteDatabase sqLiteDatabase, String user) {
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME,null);
    }

    /**
     * Performs a select all statement for a user, filtered by date
     * @param user The name of the profile we wish to preform a select all in
     * @param date The date we want to perform the select on
     * @return Cursor object which can be used to increment over the selected elements
     */
    public Cursor selectByDate(SQLiteDatabase sqLiteDatabase, String user, String date) {
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE " + COL_DATE + " LIKE " + "\'" +date + "\'";
        Log.d("DB Helper", query);
        return sqLiteDatabase.rawQuery(query,null);
    }

    public Cursor selectBetween(SQLiteDatabase sqLiteDatabase, String user, String startDate, String endDate) {
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE " + COL_DATE + " BETWEEN " + "\'" + startDate + "\'" + " AND " + "\'" + endDate + "\'";
        Log.d("DB Helper", query);
        return sqLiteDatabase.rawQuery(query,null);
    }

    /**
     * Adds test values
     */
    public void testAdd(SQLiteDatabase sqLiteDatabase) {
        Log.d("DB Helper", "Adding predefined entries");
        insert(sqLiteDatabase, MainActivity.VISION,10, "2020-11-11");
        insert(sqLiteDatabase, MainActivity.HEARING,9, "2020-11-10");
        insert(sqLiteDatabase, MainActivity.ELIMINATING,8, "2020-11-10");
        insert(sqLiteDatabase, MainActivity.MENTAL,7, "2020-11-10");
        insert(sqLiteDatabase, MainActivity.WALKING,6, "2020-11-09");
        insert(sqLiteDatabase, MainActivity.FEEDING,5, "2020-11-09");
        insert(sqLiteDatabase, MainActivity.DRESSING,4, "2020-11-09");
        insert(sqLiteDatabase, MainActivity.WALKING,3, "2020-11-08");
        insert(sqLiteDatabase, MainActivity.SPEAKING,2, "2020-11-08");
        insert(sqLiteDatabase, MainActivity.SPEAKING,1, "2020-11-06");
        insert(sqLiteDatabase, MainActivity.SPEAKING,2, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.HEARING,3, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.VISION,4, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.MENTAL,5, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.ELIMINATING,6, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.WALKING,7, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.VISION,8, "2020-11-05");
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-11-04");
        insert(sqLiteDatabase, MainActivity.SPEAKING,10, "2020-11-03");
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-11-02");
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-26");
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-27");
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-28");
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-29");
    }

    public void dropAdd(SQLiteDatabase sqLiteDatabase) {
        reset(sqLiteDatabase);
        testAdd(sqLiteDatabase);
    }

}

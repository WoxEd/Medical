package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

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

    protected static final String PROFILE_TABLE_NAME = "PROFILE";

    protected static final String DISABILITY_TABLE_NAME = "DISABILITY";

    protected static final String QUESTION_TABLE_NAME = "QUESTION";

    /**
     * Name of the column containing id
     */
    protected static final String COL_ID = "_id";

    /**
     * Name of column which holds the severity rating 1-10
     */
    protected static final String COL_RATING = "RATING";
    protected static final String COL_FK_PROFILE = "PROFILE_ID";
    protected static final String COL_FK_ENTRY = "ENTRY_ID";
    /**
     * Name of column which holds the disability name
     */
    protected static final String COL_DISABILITY = "DISABILITY";

    protected static final String COL_FIRST_NAME = "FIRST_NAME";

    protected static final String COL_LAST_NAME = "LAST_NAME";

    protected static final String COL_QUESTION = "QUESTION";
    protected static final String COL_ANSWER = "ANSWER";

    /**
     * Name of column which holds date
     */
    protected static final String COL_DATE = "ENTRY_DATE";


    private static final String CREATE_PROFILE_TABLE = "CREATE TABLE " + PROFILE_TABLE_NAME +  " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_FIRST_NAME + " text," + COL_LAST_NAME + " text)";

    private static final String CREATE_DISABILITY_TABLE = "CREATE TABLE " + DISABILITY_TABLE_NAME +
            " ("+COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_DISABILITY + " text," + COL_RATING + " INTEGER, " + COL_DATE + " text, " + COL_FK_PROFILE + " INTEGER)";

    private static final String CREATE_QUESTION_TABLE = "CREATE TABLE " + QUESTION_TABLE_NAME +  " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_QUESTION + " text, " + COL_ANSWER + " text," + COL_FK_ENTRY + " INTEGER)";
    /**
     * Insert statement
     */
    private static final String INSERT_DISABILITY = "INSERT INTO " + DISABILITY_TABLE_NAME + " (" + COL_DISABILITY + "," + COL_RATING +"," + COL_DATE+ ","+ COL_FK_PROFILE +")" + " VALUES ('%s','%d','%s', '%d')";
    private static final String SELECT_ALL_DISABILITIES = "SELECT * FROM " + DISABILITY_TABLE_NAME + " WHERE " + COL_FK_PROFILE + " = '%d'";
    private static final String SELECT_BETWEEN_DATES_STATEMENT = "SELECT * FROM " + DISABILITY_TABLE_NAME +" WHERE " + COL_DATE + " BETWEEN '%s' AND '%s' AND " + COL_FK_PROFILE + " = '%d'";
    private static final String DROP_DISABILITIES = "DROP TABLE IF EXISTS " + DISABILITY_TABLE_NAME;

    private static final String INSERT_PROFILE = "INSERT INTO " + PROFILE_TABLE_NAME + " (" + COL_FIRST_NAME + "," + COL_LAST_NAME  + ")" + " VALUES ('%s', '%s')";
    private static final String SELECT_ALL_PROFILES = "SELECT * FROM " + PROFILE_TABLE_NAME;
    private static final String DROP_PROFILE_TABLE = "DROP TABLE IF EXISTS " + PROFILE_TABLE_NAME;

    private static final String INSERT_QUESTION = "INSERT INTO " + QUESTION_TABLE_NAME + " (" + COL_QUESTION + "," + COL_ANSWER + "," + COL_FK_ENTRY + ")" + " VALUES ('%s', '%s', '%d')";
    private static final String DROP_QUESTIONS = "DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME;
    private static final String SELECT_ALL_QUESTIONS = "SELECT * FROM " + QUESTION_TABLE_NAME + " WHERE " + COL_FK_ENTRY + " = '%d'";
    /**
     * Constructor required to create database
     */
    public PrototypeOneDBOpener(Context context) {
        super(context,DATABASE_NAME,null,VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(CREATE_DISABILITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
        Log.d("DB Helper", CREATE_DISABILITY_TABLE);
        Log.d("DB Helper", CREATE_PROFILE_TABLE);
        Log.d("DB Helper", CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        reset(sqLiteDatabase);
    }

    /**
     * Insert a disability entry
     * @param sqLiteDatabase
     * @param disabilityType
     * @param severity
     * @param date
     * @param profileId
     */
    public void insert(SQLiteDatabase sqLiteDatabase, String disabilityType, int severity, String date, long profileId) {
        String insert = String.format(Locale.CANADA, INSERT_DISABILITY,disabilityType,severity,date, profileId);
        Log.d("DB Helper",insert);
        sqLiteDatabase.execSQL(insert);
    }

    /**
     * Insert a profile
     * @param sqLiteDatabase
     * @param firstName
     * @param lastName
     */
    public void insert(SQLiteDatabase sqLiteDatabase, String firstName, String lastName) {
        String insert = String.format(Locale.CANADA, INSERT_PROFILE, firstName ,lastName);
        Log.d("DB Helper",insert);
        sqLiteDatabase.execSQL(insert);
    }

    /**
     * Insert a question for an entry
     * @param sqLiteDatabase
     * @param question
     * @param answer
     * @param entryId
     */
    public void insert(SQLiteDatabase sqLiteDatabase, String question, String answer, long entryId) {
        String insert = String.format(Locale.CANADA, INSERT_QUESTION, question, answer, entryId);
        Log.d("DB Helper", insert);
        sqLiteDatabase.execSQL(insert);
    }

    public void reset(SQLiteDatabase sqLiteDatabase) {
        Log.d("DB Helper", "Dropping and recreating tables");
        sqLiteDatabase.execSQL(DROP_DISABILITIES);
        sqLiteDatabase.execSQL(DROP_PROFILE_TABLE);
        sqLiteDatabase.execSQL(CREATE_DISABILITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(DROP_QUESTIONS);
        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
    }

    /**
     * Performs a select all statement for a user
     * @param profileId Id of profile being searched for
     * @return Cursor object which can be used to increment over the selected elements
     */
    public Cursor selectAllEntry(SQLiteDatabase sqLiteDatabase, long profileId) {
        String query = String.format(Locale.CANADA, SELECT_ALL_DISABILITIES, profileId);
        return sqLiteDatabase.rawQuery(query,null);
    }

    /**
     * Performs a select all statement for profiles
     * @param sqLiteDatabase Database that will conduct the operation
     * @return Cursor object containing all profiles
     */
    public Cursor selectAll(SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.rawQuery(SELECT_ALL_PROFILES, null);
    }

    /**
     * Selects all questions from an entry
     * @param sqLiteDatabase
     * @param entryId
     * @return
     */
    public Cursor selectAll(SQLiteDatabase sqLiteDatabase, long entryId) {
        String query = String.format(Locale.CANADA, SELECT_ALL_QUESTIONS, entryId);
        return sqLiteDatabase.rawQuery(query,null);
    }

    /**
     * Performs a select between dates statement
     * @param profileId The id of the profile we wish to perform the select on
     * @param startDate The start date
     * @param endDate the end date
     * @return Cursor object which can be used to increment over selected elements
     */
    public Cursor selectBetween(SQLiteDatabase sqLiteDatabase, long profileId, String startDate, String endDate) {
        String query = String.format(SELECT_BETWEEN_DATES_STATEMENT, startDate, endDate, profileId);
        Log.d("DB Helper", query);
        return sqLiteDatabase.rawQuery(query,null);
    }

    /**
     * Adds test values
     */
    public void testAdd(SQLiteDatabase sqLiteDatabase) {
        Log.d("DB Helper", "Adding predefined entries");
        insert(sqLiteDatabase, MainActivity.VISION,10, "2020-11-11",1);
        insert(sqLiteDatabase, MainActivity.HEARING,9, "2020-11-10",1);
        insert(sqLiteDatabase, MainActivity.ELIMINATING,8, "2020-11-10",2);
        insert(sqLiteDatabase, MainActivity.MENTAL,7, "2020-11-10",1);
        insert(sqLiteDatabase, MainActivity.WALKING,6, "2020-11-09",1);
        insert(sqLiteDatabase, MainActivity.FEEDING,5, "2020-11-09",1);
        insert(sqLiteDatabase, MainActivity.DRESSING,4, "2020-11-09",1);
        insert(sqLiteDatabase, MainActivity.WALKING,3, "2020-11-08",1);
        insert(sqLiteDatabase, MainActivity.SPEAKING,2, "2020-11-08",1);
        insert(sqLiteDatabase, MainActivity.SPEAKING,1, "2020-11-06",1);
        insert(sqLiteDatabase, MainActivity.SPEAKING,2, "2020-11-05",1);
        insert(sqLiteDatabase, MainActivity.HEARING,3, "2020-11-05",1);
        insert(sqLiteDatabase, MainActivity.VISION,4, "2020-11-05",1);
        insert(sqLiteDatabase, MainActivity.MENTAL,5, "2020-11-05",1);
        insert(sqLiteDatabase, MainActivity.ELIMINATING,6, "2020-11-05",1);
        insert(sqLiteDatabase, MainActivity.WALKING,7, "2020-11-05",2);
        insert(sqLiteDatabase, MainActivity.VISION,8, "2020-11-05",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-11-04",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,10, "2020-11-03",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-11-02",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-26",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-27",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-28",2);
        insert(sqLiteDatabase, MainActivity.SPEAKING,9, "2020-10-29",2);
    }

    public void dropAdd(SQLiteDatabase sqLiteDatabase) {
        reset(sqLiteDatabase);
        testAdd(sqLiteDatabase);
    }

}

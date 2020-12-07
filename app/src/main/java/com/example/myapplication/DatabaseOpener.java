package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Locale;

/**
 * This is a database opener
 * TABLES: Profile, Disability, Questions
 */
public class DatabaseOpener extends SQLiteOpenHelper {

    /**
     * Name of database
     */
    private static final String DATABASE_NAME = "disability.db";

    /**
     * Database version number.
     */
    private static final int VERSION_NUMBER = 2;

    /**
     * Name of profile table
     */
    protected static final String PROFILE_TABLE_NAME = "PROFILE";

    /**
     * Name of disability table
     */
    protected static final String DISABILITY_TABLE_NAME = "DISABILITY";

    /**
     * Name of question table
     */
    protected static final String QUESTION_TABLE_NAME = "QUESTION";

    /**
     * Name of the column containing id used for both profile and entry
     */
    protected static final String COL_ID = "_id";

    /**
     * Name of column which holds the severity rating 1-10
     */
    protected static final String COL_RATING = "RATING";

    /**
     * Name of column for profile id
     */
    protected static final String COL_FK_PROFILE = "PROFILE_ID";

    /**
     * Name of column for entry_id as a foreign key on questions
     */
    protected static final String COL_FK_ENTRY = "ENTRY_ID";

    /**
     * Name of column which holds the disability name
     */
    protected static final String COL_DISABILITY = "DISABILITY";

    /**
     * Name of column containing first name in Profile
     */
    protected static final String COL_FIRST_NAME = "FIRST_NAME";

    /**
     * Name of column containing last name in Profile
     */
    protected static final String COL_LAST_NAME = "LAST_NAME";

    /**
     * Name of column containing question in Question
     */
    protected static final String COL_QUESTION = "QUESTION";

    /**
     * Name of column containing answer in Question
     */
    protected static final String COL_ANSWER = "ANSWER";

    /**
     * Name of column which holds date in Disability
     */
    protected static final String COL_DATE = "ENTRY_DATE";

    /**
     * Create statement for Profile
     */
    private static final String CREATE_PROFILE_TABLE = "CREATE TABLE " + PROFILE_TABLE_NAME +  " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_FIRST_NAME + " text," + COL_LAST_NAME + " text)";

    /**
     * Create statement for Disability
     */
    private static final String CREATE_DISABILITY_TABLE = "CREATE TABLE " + DISABILITY_TABLE_NAME +
            " ("+COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_DISABILITY + " text," + COL_RATING + " INTEGER, " + COL_DATE + " text, " + COL_FK_PROFILE + " INTEGER)";

    /**
     * Create statement for Question
     */
    private static final String CREATE_QUESTION_TABLE = "CREATE TABLE " + QUESTION_TABLE_NAME +  " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_QUESTION + " text, " + COL_ANSWER + " text," + COL_FK_ENTRY + " INTEGER)";

    /**
     * Insert statement for Disability
     */
    private static final String INSERT_DISABILITY = "INSERT INTO " + DISABILITY_TABLE_NAME + " (" + COL_DISABILITY + "," + COL_RATING +"," + COL_DATE+ ","+ COL_FK_PROFILE +")" + " VALUES ('%s','%d','%s','%d')";

    /**
     * Select statement for all disabilities with the same profile id
     */
    private static final String SELECT_ALL_DISABILITIES = "SELECT * FROM " + DISABILITY_TABLE_NAME + " WHERE " + COL_FK_PROFILE + " = '%d'";

    /**
     * Select statement for disabilities between two dates with the same profile id
     */
    private static final String SELECT_BETWEEN_DATES_STATEMENT = "SELECT * FROM " + DISABILITY_TABLE_NAME +" WHERE " + COL_DATE + " BETWEEN '%s' AND '%s' AND " + COL_FK_PROFILE + " = '%d'";

    /**
     * Drop statement for Disability
     */
    private static final String DROP_DISABILITIES = "DROP TABLE IF EXISTS " + DISABILITY_TABLE_NAME;

    /**
     * Insert statement for Profile
     */
    private static final String INSERT_PROFILE = "INSERT INTO " + PROFILE_TABLE_NAME + " (" + COL_FIRST_NAME + "," + COL_LAST_NAME  + ")" + " VALUES ('%s', '%s')";

    /**
     * Select statement for all profiles
     */
    private static final String SELECT_ALL_PROFILES = "SELECT * FROM " + PROFILE_TABLE_NAME;

    /**
     * Drop statement for profiles
     */
    private static final String DROP_PROFILE_TABLE = "DROP TABLE IF EXISTS " + PROFILE_TABLE_NAME;

    /**
     * Insert statement for Question
     */
    private static final String INSERT_QUESTION = "INSERT INTO " + QUESTION_TABLE_NAME + " (" + COL_QUESTION + "," + COL_ANSWER + "," + COL_FK_ENTRY + ")" + " VALUES ('%s', '%s', '%d')";

    /**
     * Drop statement for Question
     */
    private static final String DROP_QUESTIONS = "DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME;

    /**
     * Select statement for All Questions given a FK entry id
     */
    private static final String SELECT_ALL_QUESTIONS = "SELECT * FROM " + QUESTION_TABLE_NAME + " WHERE " + COL_FK_ENTRY + " = '%d'";


    /**
     * Delete statement for all Questions of a given entry
     */
    private static final String DELETE_QUESTIONS_FOR_ENTRY_ID = "DELETE FROM " + QUESTION_TABLE_NAME + " WHERE " + COL_FK_ENTRY + " = '%d'";

    /**
     * Delete statement for an entry given entry id
     */
    private static final String DELETE_ENTRY_GIVEN_ENTRY_ID = "DELETE FROM " + DISABILITY_TABLE_NAME + " WHERE " + COL_ID + " = '%d'";

    /**
     * Delete statement for a profile given profile id
     */
    private static final String DELETE_PROFILE_GIVEN_ID = "DELETE FROM " + PROFILE_TABLE_NAME + " WHERE " + COL_ID + " = '%d'";

    /**
     * Constructor required to create database
     */
    public DatabaseOpener(Context context) {
        super(context,DATABASE_NAME,null,VERSION_NUMBER);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(CREATE_DISABILITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        reset(sqLiteDatabase);
    }

    /**
     * Insert a disability entry
     */
    public void insertEntry(SQLiteDatabase sqLiteDatabase, String disabilityType, int severity, String date, long profileId) {
        String insert = String.format(Locale.CANADA, INSERT_DISABILITY,disabilityType,severity,date, profileId);
        sqLiteDatabase.execSQL(insert);
    }

    /**
     * Insert a profile
     */
    public void insertProfile(SQLiteDatabase sqLiteDatabase, String firstName, String lastName) {
        String insert = String.format(Locale.CANADA, INSERT_PROFILE, firstName ,lastName);
        sqLiteDatabase.execSQL(insert);
    }

    /**
     * Insert a question for an entry
     */
    public void insertQuestion(SQLiteDatabase sqLiteDatabase, String question, String answer, long entryId) {
        String insert = String.format(Locale.CANADA, INSERT_QUESTION, question, answer, entryId);
        sqLiteDatabase.execSQL(insert);
    }

    /**
     * Drops and recreates all tables
     */
    public void reset(SQLiteDatabase sqLiteDatabase) {
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
    public Cursor selectAllProfile(SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.rawQuery(SELECT_ALL_PROFILES, null);
    }

    /**
     * Selects all questions from an entry
     */
    public Cursor selectAllQuestions(SQLiteDatabase sqLiteDatabase, long entryId) {
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
        String query = String.format(Locale.CANADA,SELECT_BETWEEN_DATES_STATEMENT, startDate, endDate, profileId);
        return sqLiteDatabase.rawQuery(query,null);
    }

    /**
     * Deletes the questions from an entry
     */
    public void deleteQuestion(SQLiteDatabase sqLiteDatabase, long entryId) {
        String deleteQuestion = String.format(Locale.CANADA,DELETE_QUESTIONS_FOR_ENTRY_ID, entryId);
        sqLiteDatabase.execSQL(deleteQuestion);
    }

    /**
     * Deletes an entry. Deletes all the questions on the entry as well.
     */
    public void deleteEntry(SQLiteDatabase sqLiteDatabase, long entryId) {
        deleteQuestion(sqLiteDatabase, entryId);
        String deleteEntries = String.format(Locale.CANADA, DELETE_ENTRY_GIVEN_ENTRY_ID, entryId);
        sqLiteDatabase.execSQL(deleteEntries);
    }

    /**
     * Deleting a profile requires 3 steps
     * 1) Delete Questions on the Entries of the profile
     * 2) Delete the Entries on the profile after the questions are deleted
     * 3) Delete the profile
     */
    public void deleteProfile(SQLiteDatabase sqLiteDatabase, long profileId) {
        //Find all entries with FK profileId
        Cursor allEntries = selectAllEntry(sqLiteDatabase, profileId);
        //Loop through all entries getting entry id, then delete all notes with fk entry_id
        int entry_fk_index = allEntries.getColumnIndex(COL_ID);
        while(allEntries.moveToNext()) {
            long entryId = allEntries.getLong(entry_fk_index);
            deleteEntry(sqLiteDatabase, entryId);
        }

        //Finally delete profile
        String deleteProfile = String.format(Locale.CANADA, DELETE_PROFILE_GIVEN_ID, profileId);
        sqLiteDatabase.execSQL(deleteProfile);
    }
}

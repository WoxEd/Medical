package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    private static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME +
            " ("+COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_DISABILITY + " text," + COL_RATING + " INTEGER, " + COL_DATE + " text)";

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
        System.out.println("Creating table " + CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void insert(SQLiteDatabase sqLiteDatabase, String disabilityType, int severity, String date) {
        String insert = String.format(INSERT_STATEMENT,disabilityType,severity,date);
        Log.d("Insert()",insert);
        sqLiteDatabase.execSQL(insert);
    }
}

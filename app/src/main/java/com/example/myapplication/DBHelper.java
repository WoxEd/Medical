package com.example.myapplication;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    // Database name
    public static final String DATABASE_NAME = "PATIENT_D";
    // Tables names
    public static final String PATIENT_TABLE_NAME = "PATIENT_TABLE";
    public static final String DISABILITY_TABLE_NAME = "DISABILITY_TABLE";
    public static final String QUESTIONS_TABLE_NAME = "QUESTIONS_TABLE";
    public static final String HISTORY_TABLE_NAME = "HISTORY_TABLE";

    // Version
    public static final int VERSION = 1;

    // Patient table columns names
    public static final String PATIENT_ID = "P_ID";
    public static final String FIRST_NAME = "F_NAME";
    public static final String LAST_NAME = "L_NAME";
    public static final String PHONE_NUMBER = "P_NUMBER";

    // Disability table columns names
    public static final String DISABILITY_ID = "D_ID";
    public static final String DISABILITY_NAME = "D_NAME";

    // Questions table columns names
    public static final String QUESTION_ID = "Q_ID";
    public static final String QUESTION_CONTENT = "Q_CONTENT";
    public static final String DISABILITY_ID_FK = "Q_ID_D";

    //History table columns names
    public static final String HISTORY_ID = "H_ID";
    public static final String HISTORY_DATE = "H_DATE";
    public static final String HISTORY_SCORES = "H_SCORES";
    public static final String QUESTION_ID_CONTENT = "H_QUESTION";
    public static final String DISABILITY_ID_CONTENT = "H_DISABILITY";
    public static final String HISTORY_PATIENT_ID = "H_PATIENT_ID";


    //Define Query to create Patient  table
    private static final String createPatientTable = "create table " + PATIENT_TABLE_NAME + " ( "
            + PATIENT_ID + " integer primary key autoincrement, " + FIRST_NAME + " text not null, " + LAST_NAME +
            " text not null, " + PHONE_NUMBER + " text not null );";

    //Define Query to create Disability  table
    public static final String createDisabilityTable = "create table " + DISABILITY_TABLE_NAME + "( "
            + DISABILITY_ID + " integer primary key autoincrement, " + DISABILITY_NAME + " text not null );";

    //Define Query to create Questions  table
    private static final String createQuestionTable = "create table " + QUESTIONS_TABLE_NAME + " ( "
            + QUESTION_ID + " integer primary key autoincrement, " + QUESTION_CONTENT + " text not null, " +
            DISABILITY_ID_FK + " integer not null );";

    //Define Query to create History  table
    private static final String createHistoryTable = "create table " + HISTORY_TABLE_NAME + " ( "
            + HISTORY_ID + " integer primary key autoincrement, " + HISTORY_DATE + " text not null, " + HISTORY_SCORES +
            " integer not null, " + QUESTION_ID_CONTENT + " integer not null, " + DISABILITY_ID_CONTENT + " integer not null, " +
            HISTORY_PATIENT_ID + " integer not null );";



   /* public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }*/

    //Constractor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Patient table
        db.execSQL(createPatientTable);
        //Create Disability table
        db.execSQL(createDisabilityTable);
        //Insert data to Disability table
        insertDisability(db);
        //Create Questions Table
        db.execSQL(createQuestionTable);
        //Create History Table
        db.execSQL(createHistoryTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables when exists
        db.execSQL("drop table if exists " + HISTORY_TABLE_NAME);
        db.execSQL("drop table if exists " + QUESTIONS_TABLE_NAME);
        db.execSQL("drop table if exists " + DISABILITY_TABLE_NAME);
        db.execSQL("drop table if exists " + PATIENT_TABLE_NAME);
        onCreate(db);

    }

    public void insertDisability(SQLiteDatabase db) {
        //Queries to insert the Disabilities Names to table
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (1,'Vission');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (2,'Speaking');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (3,'Hearing');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (4,'Walking');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (5,'Eliminating');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (6,'Feeding');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (7,'Dressing');");
        db.execSQL("insert into DISABILITY_TABLE (D_ID, D_NAME )  values (8,'Mental');");

    }


    public boolean insertData(String fname, String lname, String phonNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(FIRST_NAME, fname);
        contentvalues.put(LAST_NAME, lname);
        contentvalues.put(PHONE_NUMBER, phonNum);
        long result = db.insert(PATIENT_TABLE_NAME, null, contentvalues);
        if (result == -1)
            return false;
        else
            return true;

    }

}


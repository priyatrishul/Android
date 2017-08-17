package com.example.priya_000.ratingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "instructors.db";
    private static final String TABLE_INSTRUCTORS = "instructors";
    private static final String COL_ID = "_id";
    private static final String COL_fNAME = "firstName";
    private static final String COL_lNAME = "lastName";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_OFFICE = "office";
    private static final String COL_AVERAGE = "average";
    private static final String COL_tRATING = "tRating";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_INSTRUCTORS + "( " +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_fNAME + " TEXT, " +
                COL_lNAME + " TEXT ," +
                COL_EMAIL + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_OFFICE + " TEXT, " +
                COL_AVERAGE + " TEXT, " +
                COL_tRATING + " TEXT " +
                " );";


        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addInstructor(Instructor instructor) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, instructor.get_id());
        values.put(COL_fNAME, instructor.get_firstName());
        values.put(COL_lNAME, instructor.get_lastName());
        values.put(COL_EMAIL, instructor.get_email());
        values.put(COL_PHONE, instructor.get_phone());
        values.put(COL_OFFICE, instructor.get_office());
        values.put(COL_AVERAGE, instructor.get_average());
        values.put(COL_tRATING, instructor.get_totalRating());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_INSTRUCTORS, null, values);
        db.close();

    }

    public Cursor fromDatabase() {
        //String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = " SELECT * FROM " + TABLE_INSTRUCTORS + " WHERE 1 ; ";
        Cursor c = db.rawQuery(query, null);
       /* c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("_id")) != null) {

            }

        }*/

        return c;
    }


}

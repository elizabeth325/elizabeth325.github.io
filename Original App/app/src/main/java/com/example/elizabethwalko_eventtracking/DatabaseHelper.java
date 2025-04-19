/**
 * DatabaseHelper is a subclass of SQLiteOpenHelper that manages database creation and version management.
 * It provides methods to perform CRUD operations on the events and users tables.
 */
// DatabaseHelper.java
package com.example.elizabethwalko_eventtracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//class to create and manage the database
public class DatabaseHelper extends SQLiteOpenHelper {
    //database name and version
    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 2;

    //table names and column names
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USERID = "user_id";

    //create table events query
    private static final String TABLE_CREATE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_USERID + " INTEGER);";

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";


    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //onCreate function
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_EVENTS);
        db.execSQL(TABLE_CREATE_USERS);
    }

    //onUpgrade function
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_EVENTS + " ADD COLUMN " + COLUMN_USERID + " INTEGER");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
        }
    }

    //insert event function to add an event to the database
    public boolean insertEvent(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DATE, date);
        int user = LoginActivity.userId;
        contentValues.put(COLUMN_USERID, user);
        long result = db.insert(TABLE_EVENTS, null, contentValues);
        checkDate(name, date);
        return result != -1;
    }

    //get all events function to retrieve all events from the database
    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        int user = LoginActivity.userId;
        return db.query(TABLE_EVENTS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DATE},
                COLUMN_USERID + "=?",
                new String[]{String.valueOf(user)},
                null, null, null);
    }

    //update event function to update an event in the database
    public boolean updateEvent(int id, String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DATE, date);
        int result = db.update(TABLE_EVENTS, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        checkDate(name, date);
        return result > 0;
    }

    //delete event function to delete an event from the database
    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EVENTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    //clear events function to delete all events from the database
    public void clearEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, null, null);
        db.close();
    }

    //insert user function to add a user to the database
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    //check date function to check if an event is happening today
    public void checkDate(String itemName, String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        try {
            Date eventDate = dateFormat.parse(date);
            Date currentDate = new Date();
            String currentDateString = dateFormat.format(currentDate);
            Date today = dateFormat.parse(currentDateString);

            if (eventDate.equals(today)) {
                SmsPermissionActivity smsInstance = new SmsPermissionActivity();
                smsInstance.sendSmsAlert(itemName + " is happening today");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //check user function to verify user credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    //get user id function to retrieve user id based on username
    public int getId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;
    }
}
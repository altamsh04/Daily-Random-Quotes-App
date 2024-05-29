package com.example.dailyquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TimerDatabaseHelper";
    private static final String DATABASE_NAME = "quotes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TIMERS = "timers";
    private static final String COLUMN_TIMER_ID = "id";
    private static final String COLUMN_TIMER_TIME = "time";

    private static final String CREATE_TIMERS_TABLE = "CREATE TABLE " + TABLE_TIMERS + " ("
            + COLUMN_TIMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TIMER_TIME + " TEXT)";

    public TimerDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating tables...");
        db.execSQL(CREATE_TIMERS_TABLE);
        // Create other tables here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!tableExists(db, TABLE_TIMERS)) {
            Log.d(TAG, "Table " + TABLE_TIMERS + " does not exist. Creating...");
            db.execSQL(CREATE_TIMERS_TABLE);
        }
    }

    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void insertOrUpdateTime(int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String time = String.format("%02d:%02d", hour, minute);
        values.put(COLUMN_TIMER_TIME, time);

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TIMER_ID + " FROM " + TABLE_TIMERS, null);
        if (cursor.moveToFirst()) {
            // Update the existing entry
            int timerId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIMER_ID));
            db.update(TABLE_TIMERS, values, COLUMN_TIMER_ID + " = ?", new String[]{String.valueOf(timerId)});
        } else {
            // Insert a new entry
            db.insert(TABLE_TIMERS, null, values);
        }
        cursor.close();
        db.close();
    }

    public String getInsertedTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_TIMER_TIME + " FROM " + TABLE_TIMERS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String time = null;

        if (cursor.moveToFirst()) {
            time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMER_TIME));
        }
        cursor.close();
        db.close();
        return time;
    }

    public void delete_inserted_time() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMERS, null, null);
        db.close();
    }

}

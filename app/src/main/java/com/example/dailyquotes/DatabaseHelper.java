package com.example.dailyquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quotes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUOTE_ID = "quoteId";
    private static final String COLUMN_QUOTE = "quote";
    private static final String COLUMN_AUTHOR = "author";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createFavoritesTable(sqLiteDatabase);
    }
    public void createFavoritesTable(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUOTE_ID + " TEXT,"
                + COLUMN_QUOTE + " TEXT,"
                + COLUMN_AUTHOR + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }
    public void createFavoritesTableIfNotExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        createFavoritesTable(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(sqLiteDatabase);
    }

    public boolean is_favorite_quote(String quoteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT 1 FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_QUOTE_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{quoteId});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public void add_favorite_quotes(String quoteId, String quote, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUOTE_ID, quoteId);
        values.put(COLUMN_QUOTE, quote);
        values.put(COLUMN_AUTHOR, author);

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public void remove_favorite_quotes(String quoteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_QUOTE_ID + " = ?", new String[]{quoteId});
        db.close();
    }

    public List<Quote> get_favorite_quotes() {
        List<Quote> favorites = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getReadableDatabase(); // Use getReadableDatabase for read operations
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int quoteIdIndex = cursor.getColumnIndex(COLUMN_QUOTE_ID);
            int quoteIndex = cursor.getColumnIndex(COLUMN_QUOTE);
            int authorIndex = cursor.getColumnIndex(COLUMN_AUTHOR);

            if (idIndex >= 0 && quoteIdIndex >= 0 && quoteIndex >= 0 && authorIndex >= 0) {
                do {
                    Quote quote = new Quote();
                    quote.setId(cursor.getInt(idIndex));
                    quote.setQuoteId(cursor.getString(quoteIdIndex));
                    quote.setQuote(cursor.getString(quoteIndex));
                    quote.setAuthor(cursor.getString(authorIndex));
                    favorites.add(quote);
                } while (cursor.moveToNext());
            } else {
                Log.e("DatabaseHelper", "Invalid column index. Check column names in the database.");
            }
        }

        cursor.close();
        db.close();
        return favorites;
    }
}

package com.example.hp.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hp.assignment.model.Even;

import java.util.ArrayList;

public class DatabaseEvenImpl implements Database<Even> {

    private static final String TABLE_NAME_EVEN = "even";
    private static final String COLUMN_EVEN_ID = "id";
    private static final String COLUMN_EVEN_TITLE = "title";
    private static final String COLUMN_EVEN_CONTENT = "content";
    private static final String COLUMN_EVEN_DATE="date";
    private static final String COLUMN_EVEN_TIME="time";
    private static final String COLUMN_EVEN_ALARM="alarm";
    private static final String COLUMN_EVEN_BACKGROUND="background";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseEvenImpl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private void openDatabase() {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
    }

    private void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    @Override
    public boolean save(Even even) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVEN_ID, even.getmId());
        values.put(COLUMN_EVEN_TITLE, even.getmTitle());
        values.put(COLUMN_EVEN_CONTENT, even.getmContent());
        values.put(COLUMN_EVEN_DATE, even.getmDate());
        values.put(COLUMN_EVEN_TIME, even.getmTime());
        values.put(COLUMN_EVEN_ALARM, even.getmAlarm());
        values.put(COLUMN_EVEN_BACKGROUND, even.getmBackground());
        long id = sqLiteDatabase.insert(TABLE_NAME_EVEN, null, values);
        closeDatabase();
        return id != -1;
    }

    @Override
    public boolean update(Even even) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVEN_TITLE, even.getmTitle());
        values.put(COLUMN_EVEN_CONTENT, even.getmContent());
        values.put(COLUMN_EVEN_DATE, even.getmDate());
        values.put(COLUMN_EVEN_TIME, even.getmTime());
        values.put(COLUMN_EVEN_ALARM, even.getmAlarm());
        values.put(COLUMN_EVEN_BACKGROUND, even.getmBackground());
        long id = sqLiteDatabase.update(TABLE_NAME_EVEN, values, "id=?", new String[]{even.getmId()});

        closeDatabase();
        return id != -1;
    }

    @Override
    public boolean delete(Even even) {
        openDatabase();

        long id = sqLiteDatabase.delete(TABLE_NAME_EVEN, "id=?", new String[]{even.getmId()}
        );

        closeDatabase();
        return id != -1;
    }

    @Override
    public ArrayList<Even> getAllData() {
        openDatabase();
        String sqlGetAllData = "SELECT * FROM " + TABLE_NAME_EVEN ;
        Cursor cursor = sqLiteDatabase.rawQuery(sqlGetAllData, null);

        ArrayList<Even> evens = new ArrayList<>();

        if (cursor == null || cursor.getCount() == 0) {
            closeDatabase();
            return evens;
        }

        cursor.moveToLast();
        int indexId = cursor.getColumnIndex(COLUMN_EVEN_ID);
        int indexTitle = cursor.getColumnIndex(COLUMN_EVEN_TITLE);
        int indexContent = cursor.getColumnIndex(COLUMN_EVEN_CONTENT);
        int indexDate=cursor.getColumnIndex(COLUMN_EVEN_DATE);
        int indexTime=cursor.getColumnIndex(COLUMN_EVEN_TIME);
        int indexAlarm=cursor.getColumnIndex(COLUMN_EVEN_ALARM);
        int indexBackground=cursor.getColumnIndex(COLUMN_EVEN_BACKGROUND);
        while (!cursor.isBeforeFirst()) {
            String id = cursor.getString(indexId);
            String title=cursor.getString(indexTitle);
            String content=cursor.getString(indexContent);
            String date=cursor.getString(indexDate);
            String time=cursor.getString(indexTime);
            String alarm=cursor.getString(indexAlarm);
            int background=cursor.getInt(indexBackground);
            evens.add(new Even(id, title, content, date, time, alarm, background));
            cursor.moveToPrevious();
        }
        cursor.close();
        closeDatabase();
        return evens;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private String sqlCreationPersonTable = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_EVEN + "(" +
                COLUMN_EVEN_ID + " TEXT PRIMARY KEY, " + COLUMN_EVEN_TITLE + " TEXT NOT NULL, " +
                COLUMN_EVEN_CONTENT + " TEXT NOT NULL, " + COLUMN_EVEN_DATE + " TEXT NOT NULL, " +
                COLUMN_EVEN_TIME + " TEXT NOT NULL, " + COLUMN_EVEN_ALARM + " TEXT NOT NULL, " +
                COLUMN_EVEN_BACKGROUND+" INTEGER"+ ")";
        private String sqlDropPersonTable = "DROP TABLE IF EXISTS " + TABLE_NAME_EVEN;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlCreationPersonTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(sqlDropPersonTable);
            onCreate(db);
        }
    }

}
package com.example.hp.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hp.assignment.model.Image;

import java.util.ArrayList;

/**
 * Created by hp on 10/23/2017.
 */

public class DatabaseImageImpl implements Database<Image> {
    private static final String TABLE_NAME_IMAGE = "image_table";
    private static final String COLUMN_IMAGE_ID = "id_image";
    private static final String COLUMN_IMAGE_INDEX = "index";
    private static final String COLUMN_IMAGE_IMAGE = "image";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseImageImpl(Context context) {
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
    public boolean save(Image image) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_ID, image.getmId());
        values.put(COLUMN_IMAGE_INDEX, image.getmIndex());
        values.put(COLUMN_IMAGE_IMAGE, image.getmImage());

        long id = sqLiteDatabase.insert(TABLE_NAME_IMAGE, null, values);
        closeDatabase();
        return id != -1;
    }

    @Override
    public boolean update(Image image) {
        openDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_IMAGE, image.getmImage());
        long id = sqLiteDatabase.update(TABLE_NAME_IMAGE, values, "id=?", new String[]{image.getmId()});

        closeDatabase();
        return id != -1;
    }

    @Override
    public boolean delete(Image image) {
        openDatabase();

        long id = sqLiteDatabase.delete(TABLE_NAME_IMAGE, "id=?", new String[]{image.getmId()}
        );

        closeDatabase();
        return id != -1;
    }

    @Override
    public ArrayList<Image> getAllData() {
        openDatabase();
        String sqlGetAllData = "SELECT * FROM " + TABLE_NAME_IMAGE;
        Cursor cursor = sqLiteDatabase.rawQuery(sqlGetAllData, null);

        ArrayList<Image> images = new ArrayList<>();

        if (cursor == null || cursor.getCount() == 0) {
            closeDatabase();
            return images;
        }

        cursor.moveToFirst();
        int indexId = cursor.getColumnIndex(COLUMN_IMAGE_ID);
        int indexIndex = cursor.getColumnIndex(COLUMN_IMAGE_INDEX);
        int indexImage = cursor.getColumnIndex(COLUMN_IMAGE_IMAGE);
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(indexId);
            String index=cursor.getString(indexIndex);
            byte[] img=cursor.getBlob(indexImage);
            images.add(new Image(id, index, img));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return images;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private String sqlCreationPersonTable = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME_IMAGE + "(" + COLUMN_IMAGE_ID + " TEXT PRIMARY KEY, " + COLUMN_IMAGE_INDEX + " INTEGER, " + COLUMN_IMAGE_IMAGE + " BLOG NOT NULL, " + ")";
        private String sqlDropPersonTable = "DROP TABLE IF EXISTS " + TABLE_NAME_IMAGE;

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

package com.example.and.mysignal;

/**
 * Created by and on 2/22/2017.
 */


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.logging.Logger;


public class MapDB extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE " + Points.MapPointsLocation.TABLE_NAME +
            " (" +
            Points.MapPointsLocation._ID + " INTEGER PRIMARY KEY, " +
            Points.MapPointsLocation.Longtitude + " TEXT, " +
            Points.MapPointsLocation.Altitude + " TEXT, " +
            Points.MapPointsLocation.Strength + " TEXT, " +
            Points.MapPointsLocation.TYPE + " TEXT " +
            ");";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS" + Points.MapPointsLocation.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MapPointsLocation.db";

    public MapDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("MyApp", "query=" + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

package com.sekhon.jason.photogallery.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class PhotoGalleryDBBuilder {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PhotoGalleryDBBuilder() {}

    /* Inner class that defines the table contents */
    public static class PhotoGalleryEntry implements BaseColumns {
        public static final String TABLE_NAME = "PhotoGalleryTable";
        public static final String COLUMN_NAME_CAPTION = "Caption";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_LONGITUDE = "Longitude";
        public static final String COLUMN_NAME_LATITUDE = "Latitude";
        public static final String COLUMN_NAME_PATH = "Path";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PhotoGalleryEntry.TABLE_NAME + " (" +
                    PhotoGalleryEntry._ID + " INTEGER PRIMARY KEY," +
                    PhotoGalleryEntry.COLUMN_NAME_CAPTION + " TEXT," +
                    PhotoGalleryEntry.COLUMN_NAME_DATE + " TEXT,"  +
                    PhotoGalleryEntry.COLUMN_NAME_LONGITUDE + " INTEGER," +
                    PhotoGalleryEntry.COLUMN_NAME_LATITUDE + " INTEGER," +
                    PhotoGalleryEntry.COLUMN_NAME_PATH + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PhotoGalleryEntry.TABLE_NAME;

}

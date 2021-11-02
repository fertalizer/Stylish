package com.mark.stylish;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;


public class StylishProvider extends ContentProvider {
    private StylishDataBaseHelper mStylishHelper;

    @Override
    public boolean onCreate() {
        mStylishHelper = new StylishDataBaseHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mStylishHelper.getWritableDatabase();

        return mStylishHelper.getAllProductInfo(database);
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri,  String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

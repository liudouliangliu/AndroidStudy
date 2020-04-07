package com.yunshitu.activitystudy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @author : liudouliang
 * @date : 2020/4/3 17:56
 * @ des   :
 */
public class MDbHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "book_provider.db";
    private static final int  DB_VERSION = 1;
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";
    private static final String CREATE_BOOK = "create table if not exists ";

    public MDbHelper( @Nullable Context context ) {
        super(context, DBNAME, null
                , DB_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {

    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

    }
}

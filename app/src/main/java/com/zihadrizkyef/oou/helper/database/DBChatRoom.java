package com.zihadrizkyef.oou.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 07/10/17.
 */

public class DBChatRoom extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ChatRoom";

    public DBChatRoom(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

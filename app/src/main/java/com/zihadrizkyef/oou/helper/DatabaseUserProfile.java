package com.zihadrizkyef.oou.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zihadrizkyef.oou.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 05/08/17.
 */

public class DatabaseUserProfile extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user";
    private static final String TABLE_NAME = "UserProfileTable";

    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";
    private static final String KEY_IMAGE_URL = "imageurl";


    public DatabaseUserProfile(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER, "+KEY_USERNAME+" TEXT, "+KEY_NAME+" TEXT, " +
                KEY_BIO+" TEXT, "+KEY_IMAGE_URL+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addUserProfile(UserProfile userProfile) {
        ContentValues value = new ContentValues();
        value.put(KEY_ID, userProfile.getId());
        value.put(KEY_USERNAME, userProfile.getUsername());
        value.put(KEY_NAME, userProfile.getName());
        value.put(KEY_BIO, userProfile.getBio());
        value.put(KEY_IMAGE_URL, userProfile.getImageUrl());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, value);
        db.close();
    }

    public UserProfile getUserProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{KEY_ID, KEY_USERNAME, KEY_NAME, KEY_BIO, KEY_IMAGE_URL};
        String[] args = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+"=?", args, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                UserProfile userProfile = new UserProfile(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
                db.close();
                return userProfile;
            }
        }
        db.close();
        return null;
    }

    public List<UserProfile> getAllUserProfiles() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<UserProfile> userProfileList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    UserProfile userProfile = new UserProfile(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4));
                    userProfileList.add(userProfile);
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return userProfileList;
    }

    public int getuserProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID}, null, null, null, null, null);
        db.close();
        return cursor.getCount();
    }

    public void updateuserProfile(int id, UserProfile userProfile) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_ID, id);
        value.put(KEY_USERNAME, userProfile.getUsername());
        value.put(KEY_NAME, userProfile.getName());
        value.put(KEY_BIO, userProfile.getBio());
        value.put(KEY_IMAGE_URL, userProfile.getImageUrl());

        db.update(TABLE_NAME, value, KEY_ID+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteuserProfile(UserProfile userProfile) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, KEY_ID+"=?", new String[]{String.valueOf(userProfile.getId())});
        db.close();
    }
}
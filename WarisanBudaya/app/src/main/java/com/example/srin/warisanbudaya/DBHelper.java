package com.example.srin.warisanbudaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SRIN on 4/14/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WBIdb.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_PHONE = "phone";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + "(" + USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERS_COLUMN_NAME +
        " TEXT, " + USERS_COLUMN_EMAIL + " TEXT, " + USERS_COLUMN_PASSWORD + " TEXT, " + USERS_COLUMN_PHONE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  USERS_TABLE_NAME);
        onCreate(db);
    }

    public void insertUser(String name, String phone, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_NAME, name);
        contentValues.put(USERS_COLUMN_PHONE, phone);
        contentValues.put(USERS_COLUMN_EMAIL, email);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        db.insert(USERS_TABLE_NAME, null, contentValues);
        db.close();
    }

    public boolean checkUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(USERS_TABLE_NAME, new String[] {USERS_COLUMN_EMAIL, USERS_COLUMN_PASSWORD}, USERS_COLUMN_EMAIL + " = ? AND " + USERS_COLUMN_PASSWORD + " = ?", new String[] {email, password}, null, null, null);
//        Cursor res =  db.rawQuery( "SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USERS_COLUMN_EMAIL + " = " + email + " AND " + USERS_COLUMN_PASSWORD +
//                " = " + password, null );

        return res.getCount() == 0 ? false : true;
    }
}

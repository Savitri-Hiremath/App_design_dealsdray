package com.example.flutter_design;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_REFERRAL_CODE = "referralCode";
    private static final String COLUMN_OTP = "otp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PHONE + " TEXT UNIQUE," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_REFERRAL_CODE + " TEXT," +
                COLUMN_OTP + " varchar(20)" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String phone, String password, String referralCode, String otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_REFERRAL_CODE, referralCode);
        values.put(COLUMN_OTP, otp);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public void addOtp(String phoneNumber, String otp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OTP, otp);
        db.update(TABLE_USERS, values, COLUMN_PHONE + "=?", new String[]{phoneNumber});
        db.close();
    }
    public String getOtp(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_OTP}, COLUMN_PHONE + "=?",
                new String[]{phoneNumber}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int otpColumnIndex = cursor.getColumnIndex(COLUMN_OTP);
            if (otpColumnIndex != -1) {
                String otp = cursor.getString(otpColumnIndex);
                cursor.close();
                return otp;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
}
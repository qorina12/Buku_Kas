package com.example.bukukasnusantara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "bkn.db";
    public static final String TABLE__NAME = "akun";
    public static final String TABLE__NAME__CASHFLOW = "cashflow";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";
    public static final String usernameAkun = "user", passwordAkun = "user";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE akun (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
//        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", "user");
        contentValues.put("password", "user");
        long res = sqLiteDatabase.insert("akun", null, contentValues);
        if (res == -1) {
            Toast.makeText(context, "Failed make akun", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sucess make akun", Toast.LENGTH_SHORT).show();
        }
//        sqLiteDatabase.close();
        sqLiteDatabase.execSQL("CREATE TABLE cashflow (ID INTEGER PRIMARY KEY AUTOINCREMENT, tanggal TEXT, nominal TEXT, keterangan TEXT, kategori TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE__NAME);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE__NAME__CASHFLOW);
        onCreate(sqLiteDatabase);
    }

    public long addUser(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", password);
        long res = db.insert("akun", null, contentValues);
        db.close();
        return res;
    }

    public long addData(String tanggal, String nominal, String keterangan, String kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tanggal", tanggal);
        contentValues.put("nominal", nominal);
        contentValues.put("keterangan", keterangan);
        contentValues.put("kategori", kategori);
        long res = db.insert("cashflow", null, contentValues);
        if (res == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Sucess", Toast.LENGTH_SHORT).show();
        }
        db.close();
        return res;
    }

    public boolean checkuser(String username, String password) {
        String[] columns = { COL_1} ;
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE__NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count>0)
            return true;
        else
            return false;
    }

    public boolean checkuser2(String username, String password) {
        Log.d("Username input", username);
        Log.d("Username", usernameAkun);
        Log.d("Password input", password);
        Log.d("Password", passwordAkun);
        if (username.equals(usernameAkun) && password.equals(passwordAkun)){
            return true;
        } else {
            return false;
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE__NAME__CASHFLOW;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean changePassword(String oldPass, String newPass) {
        String[] columns = { COL_1} ;
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = { "user", oldPass };
        Cursor cursor = db.query(TABLE__NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
//        db.close();

        if (count==0) {
            return false;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("password", newPass);
            long res = db.update(TABLE__NAME, contentValues, "username = ?", new String[]{"user"});
            if (res == -1) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Sucess", Toast.LENGTH_SHORT).show();
            }
            db.close();
            return true;
        }
    }

//    public void insertToData(long valX, int valY){
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put();
//    }
}

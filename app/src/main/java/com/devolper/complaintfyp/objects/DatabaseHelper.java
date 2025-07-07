package com.devolper.complaintfyp.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by waqas ahmed on 5/4/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "complaint_fyp.db";
    public static final String TABLE_PROFILE= "profile";
    private String offline_name,offline_email;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table IF NOT EXISTS " + TABLE_PROFILE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,email TEXT,username TEXT,gender TEXT,regno TEXT,status TEXT,type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS" + TABLE_BOOKMARKS);
        onCreate(db);
    }

    public boolean insertProfile(String name,String email,String username, String gender,String regno,String status, String type){
        SQLiteDatabase db0 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", String.valueOf(name));
        contentValues.put("email", String.valueOf(email));
        contentValues.put("username", String.valueOf(username));
        contentValues.put("gender", String.valueOf(gender));
        contentValues.put("regno", String.valueOf(regno));
        contentValues.put("status", String.valueOf(status));
        contentValues.put("type", String.valueOf(type));
        long isInserted = db0.insert(TABLE_PROFILE,null,contentValues);
        if(isInserted == -1)
            return false;
        else
            return true;
    }

    public boolean emptyOffline(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("delete from " + TABLE_PROFILE ,null);
        if(res.getCount() > 0)
            return true;

        return false;
    }



    public ArrayList getProfileData(){
        ArrayList a = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PROFILE ,null);
        if(res.getCount() > 0){
            while (res.moveToNext()) {
                a.add(res.getString(1));
                a.add(res.getString(2));
                a.add(res.getString(3));
                a.add(res.getString(4));
                a.add(res.getString(5));
                a.add(res.getString(6));
                a.add(res.getString(7));
            }
        }
        return a;

    }

}

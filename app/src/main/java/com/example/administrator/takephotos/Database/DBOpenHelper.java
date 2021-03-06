package com.example.administrator.takephotos.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hasee on 2016/3/18.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "test.db";
    private static int DB_VERSION = 1;

    public DBOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, password TEXT,sex TEXT);");  // 用户表

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "USERINFO (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, height FLOAT, " +
                "breast FLOAT, waist FLOAT, hipshot FLOAT, sex TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS CACHE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userName TEXT, password TEXT, flag INTEGER, auto INTEGER);");  // 登录信息缓存表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

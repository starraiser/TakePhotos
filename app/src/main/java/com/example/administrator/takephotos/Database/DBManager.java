package com.example.administrator.takephotos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.takephotos.Entity.Info;

import java.util.List;

/**
 * Created by hasee on 2016/3/18.
 */
public class DBManager {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    private List<Info> list;

    public DBManager(Context context){
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void addUser(Info info){
        String name = info.get_name();
        double height = info.get_height();
        double breast = info.get_breast();
        double waist = info.get_waist();
        double hipshot = info.get_hipshot();
        String sex = info.get_sex();
        db.execSQL("insert into USER values(null,?,?,?,?,?,?)",
                new Object[]{name,height,breast,waist,hipshot,sex});
    }

    public Info getInfoByName(String name){
        Info info = new Info();
        ContentValues cv = new ContentValues();
        Cursor cursor = db.rawQuery("select * from USER where userName = ?",new String[]{name});
        while(cursor.moveToNext()){
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            double height = cursor.getDouble(cursor.getColumnIndex("height"));
            double breast = cursor.getDouble(cursor.getColumnIndex("breast"));
            double waist = cursor.getDouble(cursor.getColumnIndex("waist"));
            double hipshot = cursor.getDouble(cursor.getColumnIndex("hipshot"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            info = new Info(_id,userName,height,breast,waist,hipshot,sex);
        }
        return info;
    }
}

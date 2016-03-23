package com.example.administrator.takephotos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.takephotos.Entity.Info;
import com.example.administrator.takephotos.Entity.User;

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

    public void addUser(User user){  // 添加用户
        String name = user.getUserName();
        String password = user.getPassword();
        db.execSQL("insert into USER values(null,?,?)",
                new Object[]{name, password});
    }

    public boolean checkUser(String name, String password){  // 检查用户是否存在or密码是否正确
        String userPassword="";
        ContentValues cv = new ContentValues();
        Cursor cursor = db.rawQuery("select * from USER where userName=?",new String[]{name});

        if(cursor.getCount() == 0){  // 判断是否存在该用户
            return false;
        }

        while(cursor.moveToNext()){
            userPassword = cursor.getString(cursor.getColumnIndex("password"));  // 获取用户名对应密码
        }

        if(userPassword.equals(password)){  // 密码正确
            return true;
        } else{  // 密码错误
            return false;
        }
    }

    public int getIdByName(String name){  // 通过用户名查找用户id
        int id=-1;
        ContentValues cv = new ContentValues();
        Cursor cursor = db.rawQuery("select _id from USER where userName=?",new String[]{name});
        while(cursor.moveToNext()){
            id = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        return id;
    }

    public String getNameById(int id){  // 通过用户名查找用户id
        String name="";
        ContentValues cv = new ContentValues();
        Cursor cursor = db.rawQuery("select userName from USER where _id=?",new String[]{String.valueOf(id)});
        while(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex("userName"));
        }
        return name;
    }

    public void addUserInfo(Info info){
        int userId = info.get_userId();
        double height = info.get_height();
        double breast = info.get_breast();
        double waist = info.get_waist();
        double hipshot = info.get_hipshot();
        String sex = info.get_sex();
        db.execSQL("insert into USERINFO values(null,?,?,?,?,?,?)",
                new Object[]{userId,height,breast,waist,hipshot,sex});
    }

    public Info getInfoById(int userId){
        Info info = new Info();
        ContentValues cv = new ContentValues();
        Cursor cursor = db.rawQuery("select * from USERINFO where userId = ?",new String[]{String.valueOf(userId)});
        while(cursor.moveToNext()){
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            int _userId = cursor.getInt(cursor.getColumnIndex("userId"));
            double height = cursor.getDouble(cursor.getColumnIndex("height"));
            double breast = cursor.getDouble(cursor.getColumnIndex("breast"));
            double waist = cursor.getDouble(cursor.getColumnIndex("waist"));
            double hipshot = cursor.getDouble(cursor.getColumnIndex("hipshot"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            info = new Info(_id,_userId,height,breast,waist,hipshot,sex);
        }
        return info;
    }
}

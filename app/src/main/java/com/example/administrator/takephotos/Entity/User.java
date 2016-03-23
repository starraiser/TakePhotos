package com.example.administrator.takephotos.Entity;

/**
 * Created by Administrator on 2016/1/6.
 */
public class User {

    private int userId;
    private String userName;
    private String password;
    private String sex;

    public User(String userName, String password,String sex) {
        this.userName = userName;
        this.password = password;
        this.sex = sex;
    }

    public void setUserName(String name){
        userName = name;
    }

    public String getUserName(){
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }
}

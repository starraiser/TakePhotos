package com.example.administrator.takephotos.Entity;

/**
 * Created by hasee on 2016/3/18.
 */
public class Info {
    private int _id;
    private int _userId;
    private double _height;
    private double _breast;
    private double _waist;
    private double _hipshot;
    private String _sex;

    public Info(){}

    public Info(int id,int userId,double height,double breast,double waist,double hipshot,String sex){
        this._id = id;
        this._userId = userId;
        this._height = height;
        this._breast = breast;
        this._waist = waist;
        this._hipshot = hipshot;
        this._sex = sex;
    }

    public Info(double height){
        this._height = height;
    }

    public int get_id() {
        return _id;
    }

    public int get_userId() {
        return this._userId;
    }

    public double get_breast() {
        return _breast;
    }

    public double get_height() {
        return _height;
    }

    public double get_hipshot() {
        return _hipshot;
    }

    public double get_waist() {
        return _waist;
    }

    public String get_sex() {
        return _sex;
    }

    public void set_sex(String _sex) {
        this._sex = _sex;
    }

    public void set_userId(int userId) {
        this._userId = userId;
    }

    public void set_breast(double _breast) {
        this._breast = _breast;
    }

    public void set_height(double _height) {
        this._height = _height;
    }

    public void set_hipshot(double _hipshot) {
        this._hipshot = _hipshot;
    }

    public void set_waist(double _waist) {
        this._waist = _waist;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_breast(double width,double thickness){
        double x1 = width;
        double x2 = thickness;
        double x3 = x1*x1;
        double x4 = x2*x2;
        double x5 = x1*x2;
        if(this._sex == "man"){
            this._breast = 128.83-2.112*x1-4.232*x2-0.054*x3-0.063*x4+0.291*x5;
        } else {
            this._breast = -7.677+1.853*x1+1.748*x2+0.004*x3+0.005*x4-0.010*x5;
        }
    }

    public void set_waist(double width,double thickness){
        double x1 = width;
        double x2 = thickness;
        double x3 = x1*x1;
        double x4 = x2*x2;
        double x5 = x1*x2;
        if(this._sex == "man"){
            this._breast = 101.99-4.801*x1+0.294*x2+0.014*x3-0.152*x4+0.277*x5;
        } else {
            this._breast = 1.933+1.756*x1+1.122*x2+0.009*x4-0.002*x5;
        }
    }

    public void set_hipshot(double width,double thickness){
        double x1 = width;
        double x2 = thickness;
        double x3 = x1*x1;
        double x4 = x2*x2;
        double x5 = x1*x2;
        if(this._sex == "man"){
            this._breast = 217.07-9.616*x1-0.366*x2+0.174*x3+0.052*x4-0.026*x5;
        } else {
            this._breast = 4.049+2.465*x1-0.025*x3+0.043*x5;
        }
    }
}

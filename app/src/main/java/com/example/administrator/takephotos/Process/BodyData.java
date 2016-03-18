package com.example.administrator.takephotos.Process;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/3/18.
 */
public class BodyData {

    private float height;  // 身高
    private Bitmap frontImg;  // 正面照
    private Bitmap sideImg;  // 侧面照

    public BodyData(float height,Bitmap front, Bitmap side){
        this.height = height;
        this.frontImg = front;
        this.sideImg = side;
    }

    public float getBreastThickness(){
        float thickness = 0;
        return thickness;
    }

    public float getBreastWidth(){
        float width = 0;
        return width;
    }

    public float getWaistThickness(){
        float thickness = 0;
        return thickness;
    }

    public float getWaistWidth(){
        float width = 0;
        return width;
    }

    public float getHipshotThickness(){
        float thickness = 0;
        return thickness;
    }

    public float getHipshotWidth(){
        float width = 0;
        return width;
    }
}

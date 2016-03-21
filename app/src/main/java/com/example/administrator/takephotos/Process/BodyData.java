package com.example.administrator.takephotos.Process;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Administrator on 2016/3/18.
 */
public class BodyData {

    private float height;  // 身高
    private int frontHeightPixel;
    private int sideHeightPixel;
    private int headHeight;
    private Bitmap frontImg;  // 正面照
    private Bitmap sideImg;  // 侧面照
    private int breastOffset;  // 胸部偏移量
    private int waistOffset;  // 腰部偏移量
    private int hipshotOffset;  // 臀部偏移量
    private int jawOffset; // 下巴偏移量
    private int frontHeadLine;  // 正面头顶位置
    private int sideHeadLine;  // 侧面头顶位置
    private int[] frontBlack;
    private int[] sideBlack;

    public BodyData(float height,Bitmap front, Bitmap side){
        this.height = height;
        this.frontImg = front;
        this.sideImg = side;
        init(front, side);
    }

    public void init(Bitmap front,Bitmap side){

        frontBlack = new int[front.getHeight()];
        sideBlack = new int [side.getHeight()];
        setFrontHeadLine(front);
        setSideHeadLine(side);

        if(getFrontFootLine(front)!=0){  // 正面身高像素
            frontHeightPixel = frontHeadLine - getFrontFootLine(front) + 1;
        } else {
            frontHeightPixel = -1;
        }

        if(getSideFootLine(side)!=0){  // 侧面身高像素
            sideHeightPixel = sideHeadLine - getSideFootLine(side) + 1;
        } else {
            sideHeightPixel = -1;
        }

        for(int i = 0;i<front.getHeight();i++){  // 正面每行黑点数
            for(int j = 0;j<front.getWidth();i++){
                if(Color.red(front.getPixel(i,j))==255)
                frontBlack[i]++;
            }
        }

        for(int i = 0;i<side.getHeight();i++){  // 侧面每行黑点数
            for(int j = 0;j<side.getWidth();i++){
                if(Color.red(side.getPixel(i,j))==255)
                    frontBlack[i]++;
            }
        }
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

    public void getJawOffset(Bitmap img){
        
    }

    public int getFrontFootLine(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = height-1;i>0;i++){
            for(int j=0;j<width;j++){
                if (Color.red(img.getPixel(i,j))==255){
                    return i;
                }
            }
        }
        return -1;
    }

    public int getSideFootLine(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = height-1;i>0;i++){
            for(int j=0;j<width;j++){
                if (Color.red(img.getPixel(i,j))==255){
                    return i;
                }
            }
        }
        return -1;
    }

    // 获取正面照头顶位置
    public int setFrontHeadLine(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = 0;i<height;i++){
            for(int j=0;j<width;j++){
                if (Color.red(img.getPixel(i,j))==255){
                    frontHeadLine = i;
                    return i;
                }
            }
        }
        return frontHeadLine;
    }

    // 获取侧面照头顶位置
    public int setSideHeadLine(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = 0;i<height;i++){
            for(int j=0;j<width;j++){
                if (Color.red(img.getPixel(i,j))==255){
                    sideHeadLine = i;
                    return i;
                }
            }
        }
        return sideHeadLine;
    }
}

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
    private int frontHeadHeight;
    private int sideHeadHeight;
    private Bitmap frontImg;  // 正面照
    private Bitmap sideImg;  // 侧面照
    private int frontBreastOffset;  // 胸部偏移量
    private int frontWaistOffset;  // 腰部偏移量
    private int frontHipshotOffset;  // 臀部偏移量
    private int frontJawOffset; // 下巴偏移量
    private int sideBreastOffset;  // 胸部偏移量
    private int sideWaistOffset;  // 腰部偏移量
    private int sideHipshotOffset;  // 臀部偏移量
    private int sideJawOffset;
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
            frontHeightPixel = getFrontFootLine(front) - frontHeadLine + 1;
        } else {
            frontHeightPixel = -1;
        }

        if(getSideFootLine(side)!=0){  // 侧面身高像素
            sideHeightPixel = getSideFootLine(side) - sideHeadLine + 1;
        } else {
            sideHeightPixel = -1;
        }

        for(int i = 0;i<front.getHeight();i++){  // 正面每行黑点数
            for(int j = 0;j<front.getWidth();j++){
                if(Color.red(front.getPixel(j,i))==255)
                frontBlack[i]++;
            }
        }

        for(int i = 0;i<side.getHeight();i++){  // 侧面每行黑点数
            for(int j = 0;j<side.getWidth();j++){
                if(Color.red(side.getPixel(j,i))==255)
                    sideBlack[i]++;
            }
        }
        setFrontJawOffset(front);
        setSideJawOffset(side);
        frontHeadHeight = frontJawOffset - frontHeadLine + 1;
        sideHeadHeight = sideJawOffset - sideHeadLine + 1;
    }

    public float getBreastThickness(){
        float thickness = 0;
        sideBreastOffset = sideHeadLine + sideHeadHeight*2;
        int maxBlack = sideBlack[sideBreastOffset];
        int maxPosition = sideBreastOffset;
        for(int i =sideBreastOffset-20;i<sideBreastOffset+20;i++){
            if(i<0){
                i=0;
            }
            if(sideBlack[i]>maxBlack){
                maxBlack = sideBlack[i];
                maxPosition = i;
            }
        }

        thickness = maxBlack*(height/sideHeightPixel);
        sideBreastOffset = maxPosition;
        return thickness;
    }

    public float getBreastWidth(){
        float width = 0;
        getBreastThickness();
        frontBreastOffset = ((sideBreastOffset-sideHeadLine)/sideHeightPixel)*frontHeightPixel+frontHeadLine;
        width = frontBlack[frontBreastOffset];
        width = width*(height/frontHeightPixel);
        return width;
    }

    public float getWaistThickness(){
        float thickness = 0;
        getWaistWidth();
        sideWaistOffset = ((frontWaistOffset-frontHeadLine)/frontHeightPixel)*sideHeightPixel+sideHeadLine;
        thickness = sideBlack[sideWaistOffset];
        thickness = thickness*(height/sideHeightPixel);

        return thickness;
    }

    public float getWaistWidth(){
        float width = 0;
        frontBreastOffset = frontHeadLine + frontHeadHeight*(3/8);
        int minBlack = frontBlack[frontBreastOffset];
        int minPosition = frontBreastOffset;
        for(int i =frontBreastOffset-20;i<frontBreastOffset+20;i++){
            if(i<0){
                i=0;
            }
            if(frontBlack[i]<minBlack){
                minBlack = frontBlack[i];
                minPosition = i;
            }
        }
        width = minBlack*(height/frontHeightPixel);
        frontWaistOffset = minPosition;

        return width;
    }

    public float getHipshotThickness(){
        float thickness = 0;
        getHipshotWidth();
        sideHipshotOffset = ((frontHipshotOffset-frontHeadLine)/frontHeightPixel)*sideHeightPixel+sideHeadLine;
        thickness = sideBlack[sideHipshotOffset];
        thickness = thickness*(height/sideHeightPixel);

        return thickness;
    }

    public float getHipshotWidth(){
        float width = 0;
        frontHipshotOffset = frontHeadLine + frontHeadHeight*(3/8);
        int maxBlack = frontBlack[frontHipshotOffset];
        int maxPosition = frontHipshotOffset;
        for(int i =frontHipshotOffset-20;i<frontHipshotOffset+20;i++){
            if(i<0){
                i=0;
            }
            if(frontBlack[i]>maxBlack){
                maxBlack = frontBlack[i];
                maxPosition = i;
            }
        }
        width = maxBlack*(height/frontHeightPixel);
        frontWaistOffset = maxPosition;

        return width;
    }

    public void setFrontJawOffset(Bitmap img){
        for(int i=frontHeadLine+frontHeightPixel/5;i>frontHeadLine;i--){
            for(int j=1;j<11;j++){
                if(j>i){
                    frontJawOffset = frontHeadLine+frontHeightPixel/5;
                    return;
                }
                if(frontBlack[i-j]<frontBlack[i]){
                    break;
                }
                if(j==10){
                    frontJawOffset = i;
                    return;
                }
            }
        }
        return;
    }

    public void setSideJawOffset(Bitmap img){
        for(int i=sideHeadLine+sideHeightPixel/5;i>sideHeadLine;i--){
            for(int j=1;j<11;j++){
                if(j>i){
                    frontJawOffset = frontHeadLine+frontHeightPixel/5;
                    return;
                }
                if(sideBlack[i-j]<sideBlack[i]){
                    break;
                }
                if(j==10){
                    sideJawOffset = i;
                    return;
                }
            }
        }
        return;
    }

    public int getFrontFootLine(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();
        for(int i = height-1;i>0;i++){
            for(int j=0;j<width;j++){
                if (Color.red(img.getPixel(j,i))==255){
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
                if (Color.red(img.getPixel(j,i))==255){
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
                if (Color.red(img.getPixel(j,i))==255){
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
                if (Color.red(img.getPixel(j,i))==255){
                    sideHeadLine = i;
                    return i;
                }
            }
        }
        return sideHeadLine;
    }
}

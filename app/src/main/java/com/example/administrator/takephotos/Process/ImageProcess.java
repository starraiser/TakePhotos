package com.example.administrator.takephotos.Process;

import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by hasee on 2016/2/5.
 */
public class ImageProcess {
    ImageProcess(){

    }

    public Bitmap toGrey(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();

        Bitmap outputImg = Bitmap.createBitmap(width, height, img.getConfig());
        //int[] pix = new int[width*height];
        //int index = 0;
        //img.getPixels(pix,0,width,0,0,width,height);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = img.getPixel(i, j);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);
                r = g = b = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                outputImg.setPixel(i, j, Color.rgb(r, g, b));
            }
        }
        return outputImg;
    }

    public Bitmap Gaussian(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();

        Bitmap outputImg = Bitmap.createBitmap(width, height, img.getConfig());

        return outputImg;
    }
}

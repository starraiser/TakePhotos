package com.example.administrator.takephotos.Process;

/**
 * Created by Administrator on 2016/2/16.
 */
//import java.awt.image.BufferedImage;
import android.graphics.Bitmap;
import android.graphics.Color;

public class OtsuBinaryFilter {

    public OtsuBinaryFilter() {
        System.out.println("Otsu");
    }

    //阈值二值化
    public Bitmap filter2(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = null;
        if (dest == null)
            dest = Bitmap.createBitmap(width, height, src.getConfig());

        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        src.getPixels(inPixels, 0, width, 0, 0, width, height);
        int index = 0;

        for (int row = 0; row < height; row++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;
                int gray = (int) (0.299 * tr + 0.587 * tg + 0.114 * tb);
                inPixels[index] = (ta << 24) | (gray << 16) | (gray << 8) | gray;
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                index = row * width + col;
                int gray = (inPixels[index] >> 8) & 0xff;
                if (gray > 10) {
                    gray = 255;
                    outPixels[index] = (0xff << 24) | (gray << 16) | (gray << 8) | gray;
                } else {
                    gray = 0;
                    outPixels[index] = (0xff << 24) | (gray << 16) | (gray << 8) | gray;
                }

            }
        }
        dest.setPixels(outPixels, 0, width, 0, 0, width, height);
        return  dest;
    }
    //OTSU二值化操作
    public Bitmap filter(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = null;
        if ( dest == null )
            dest = Bitmap.createBitmap(width, height, src.getConfig());
        // 图像灰度化
        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        src.getPixels(inPixels, 0, width, 0, 0, width, height);
        int index = 0;
        for(int row=0; row<height; row++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;
            for(int col=0; col<width; col++) {
                index = row * width + col;
                ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;
                int gray= (int)(0.299 *tr + 0.587*tg + 0.114*tb);
                inPixels[index]  = (ta << 24) | (gray << 16) | (gray << 8) | gray;
            }
        }
        // 获取直方图
        int[] histogram = new int[256];
        for(int row=0; row<height; row++) {
            int tr = 0;
            for(int col=0; col<width; col++) {
                index = row * width + col;
                tr = (inPixels[index] >> 16) & 0xff;
                histogram[tr]++;
            }
        }
        // 图像二值化 - OTSU 阈值化方法
        double total = width * height;
        double[] variances = new double[256];
        for(int i=0; i<variances.length; i++)
        {
            double bw = 0;
            double bmeans = 0;
            double bvariance = 0;
            double count = 0;
            for(int t=0; t<i; t++)
            {
                count += histogram[t];
                bmeans += histogram[t] * t;
            }
            bw = count / total;
            bmeans = (count == 0) ? 0 :(bmeans / count);
            for(int t=0; t<i; t++)
            {
                bvariance += (Math.pow((t-bmeans),2) * histogram[t]);
            }
            bvariance = (count == 0) ? 0 : (bvariance / count);
            double fw = 0;
            double fmeans = 0;
            double fvariance = 0;
            count = 0;
            for(int t=i; t<histogram.length; t++)
            {
                count += histogram[t];
                fmeans += histogram[t] * t;
            }
            fw = count / total;
            fmeans = (count == 0) ? 0 : (fmeans / count);
            for(int t=i; t<histogram.length; t++)
            {
                fvariance += (Math.pow((t-fmeans),2) * histogram[t]);
            }
            fvariance = (count == 0) ? 0 : (fvariance / count);
            variances[i] = bw * bvariance + fw * fvariance;
        }

        // find the minimum within class variance
        double min = variances[0];
        int threshold = 0;
        for(int m=1; m<variances.length; m++)
        {
            if(min > variances[m]){
                threshold = m;
                min = variances[m];
            }
        }
        // 二值化
        System.out.println("final threshold value : " + threshold);
        for(int row=0; row<height; row++) {
            for(int col=0; col<width; col++) {
                index = row * width + col;
                int gray = (inPixels[index] >> 8) & 0xff;
                if(gray > threshold)
                {
                    gray = 255;
                    outPixels[index]  = (0xff << 24) | (gray << 16) | (gray << 8) | gray;
                }
                else
                {
                    gray = 0;
                    outPixels[index]  = (0xff << 24) | (gray << 16) | (gray << 8) | gray;
                }

            }
        }
        dest.setPixels(outPixels,0,width,0,0,width,height);
        return dest;
    }

    //提取二值化后的图片的轮廓
    public Bitmap edge(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();


        Bitmap dest = Bitmap.createBitmap(width, height, src.getConfig());
        //二值化后图像
        Bitmap temp = Bitmap.createBitmap(width, height, src.getConfig());
        //dest = filter(src,dest);
        temp = filter(src);
        //return temp;

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = temp.getPixel(i, j);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    dest.setPixel(i,j,Color.WHITE);
                }
                else{
                    int pixel1 = temp.getPixel(i - 1, j - 1);
                    int pixel2 = temp.getPixel(i - 1, j);
                    int pixel3 = temp.getPixel(i - 1, j + 1);
                    int pixel4 = temp.getPixel(i, j - 1);
                    int pixel5 = temp.getPixel(i, j + 1);
                    int pixel6 = temp.getPixel(i + 1, j - 1);
                    int pixel7 = temp.getPixel(i + 1, j);
                    int pixel8 = temp.getPixel(i + 1, j + 1);
                    if(isBlack(pixel1)&&isBlack(pixel2)&&isBlack(pixel3)&&isBlack(pixel4)&&isBlack(pixel5)&&isBlack(pixel6)&&isBlack(pixel7)&&isBlack(pixel8)&&isBlack(pixel)){
                        dest.setPixel(i,j,Color.WHITE);
                    }
                    else{
                        dest.setPixel(i, j, Color.rgb(r, g, b));
                    }
                }
            }
        }

        return dest;
    }


    // 开运算
    public Bitmap open(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = Bitmap.createBitmap(width, height, src.getConfig());

        Bitmap binaryBitmap = filter(src);
        Bitmap rustBitmap = rust(binaryBitmap);
        dest = swell(rustBitmap);
        return dest;
    }

    public Bitmap close(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = Bitmap.createBitmap(width, height, src.getConfig());

        Bitmap binaryBitmap = filter(src);
        Bitmap swellBitmap = rust(binaryBitmap);
        dest = rust(swellBitmap);
        return dest;
    }
    // 闭运算
    // 膨胀
    public Bitmap swell(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = Bitmap.createBitmap(width, height, src.getConfig());
        //二值化后图像
        //Bitmap temp = Bitmap.createBitmap(width, height, src.getConfig());
        Bitmap temp = null;
        //dest = filter(src,dest);
        temp = filter(src);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = temp.getPixel(i, j);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    dest.setPixel(i,j,Color.WHITE);
                }
                else{
                    int pixel1 = temp.getPixel(i - 1, j - 1);
                    int pixel2 = temp.getPixel(i - 1, j);
                    int pixel3 = temp.getPixel(i - 1, j + 1);
                    int pixel4 = temp.getPixel(i, j - 1);
                    int pixel5 = temp.getPixel(i, j + 1);
                    int pixel6 = temp.getPixel(i + 1, j - 1);
                    int pixel7 = temp.getPixel(i + 1, j);
                    int pixel8 = temp.getPixel(i + 1, j + 1);
                    if(isBlack(pixel1)&&isBlack(pixel2)&&isBlack(pixel3)&&isBlack(pixel4)&&isBlack(pixel5)&&isBlack(pixel6)&&isBlack(pixel7)&&isBlack(pixel8)){
                        dest.setPixel(i,j,Color.BLACK);
                    }
                    else{
                        dest.setPixel(i, j, Color.WHITE);
                    }
                }
            }
        }

        return dest;
    }

    // 腐蚀
    public Bitmap rust(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = Bitmap.createBitmap(width, height, src.getConfig());
        //Bitmap temp = Bitmap.createBitmap(width, height, src.getConfig());
        Bitmap temp = null;
        //dest = filter(src,dest);
        temp = filter(src);

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int pixel = temp.getPixel(i, j);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    dest.setPixel(i,j,Color.WHITE);
                }
                else{
                    int pixel1 = temp.getPixel(i - 1, j - 1);
                    int pixel2 = temp.getPixel(i - 1, j);
                    int pixel3 = temp.getPixel(i - 1, j + 1);
                    int pixel4 = temp.getPixel(i, j - 1);
                    int pixel5 = temp.getPixel(i, j + 1);
                    int pixel6 = temp.getPixel(i + 1, j - 1);
                    int pixel7 = temp.getPixel(i + 1, j);
                    int pixel8 = temp.getPixel(i + 1, j + 1);
                    if(isWhite(pixel1)&&isWhite(pixel2)&&isWhite(pixel3)&&isWhite(pixel4)&&isWhite(pixel5)&&isWhite(pixel6)&&isWhite(pixel7)&&isWhite(pixel8)){
                        dest.setPixel(i,j,Color.WHITE);
                    }
                    else{
                        dest.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        }
        return dest;
    }

    /**
     * 求外接矩形
     * @param src 源图片
     * @param x1 计算部位左上角的x坐标
     * @param y1 计算部位左上角的y坐标
     * @param x2 计算部位右下角的x坐标
     * @param y2 计算部位右下角的y坐标
     * @return 返回外接矩形的左上角、右下角的坐标，以int数组存储依次为左上角的x坐标，左上角的y坐标，
     *         右下角的x坐标，右下角的y坐标
     */
    public int[] rectangle(Bitmap src,int x1, int y1, int x2, int y2){
        int[] vertex = new int[4];
        for(int i = x1; i < x2; i++){
            for(int j = y1; j < y2; j++){
                int pixel = src.getPixel(i, j);
                if(pixel == Color.BLACK){
                    vertex[0] = i;
                }
            }
        }
        for(int i = y1; i < y2; i++){
            for(int j = x1; j < x2; j++){
                int pixel = src.getPixel(i, j);
                if(pixel == Color.BLACK){
                    vertex[1] = i;
                }
            }
        }
        for(int i = x2; i > x1; i--){
            for(int j = y1; j < y2; j++){
                int pixel = src.getPixel(i, j);
                if(pixel == Color.BLACK){
                    vertex[2] = i;
                }
            }
        }
        for(int i = y2; i > x1; i--){
            for(int j = x1; j < x2; j++){
                int pixel = src.getPixel(i, j);
                if(pixel == Color.BLACK){
                    vertex[3] = i;
                }
            }
        }
        return vertex;
    }

    /**
     * 判断像素点是否为黑色
     * @param pixel 像素值
     * @return 返回布尔变量
     */
    public boolean isBlack(int pixel){
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        if(r == 0 && g == 0 && b == 0){
            return true;
        }
        return false;
    }

    /**
     * 判断像素点是否为白色
     * @param pixel 像素值
     * @return 返回布尔变量
     */
    public boolean isWhite(int pixel){
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        if(r == 255 && g == 255 && b == 255){
            return true;
        }
        return false;
    }

}

package com.example.administrator.takephotos;

/**
 * @author  star
 * @date 2015.12
 */
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.takephotos.Process.OtsuBinaryFilter;

import uk.co.senab.photoview.PhotoView;

public class Pager extends Activity {
    private final int PHOTO_1 = 1;
    private final int PHOTO_2 = 2;
    private final int UPLOAD = 3;

    //private Animation animationTranslate, animationRotate, animationScale;
    //private static int width, height;
    //private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
    //private android.support.design.widget.CoordinatorLayout.LayoutParams params = new android.support.design.widget.CoordinatorLayout.LayoutParams(0, 0);
    //private static Boolean isClick = false;

    private View view1, view2;
    private List<View> viewList;// view数组
    private ViewPager viewPager; // 对应的viewPager
    private int page;

    //显示图片的ImageView
    private ImageView img1;
    private PhotoView photoView;
    private ImageView img2;

    //图片bitmap
    Bitmap bitmap1;
    Bitmap bitmap2;

    //图片文件夹路径
    private String savePath;

    //图片文件
    private File pic1File;
    private File pic2File;

    //ViewPager标题
    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        final FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.editFab);
        //final FloatingActionButton cancelFab = (FloatingActionButton) findViewById(R.id.cancelFab);
        final FloatingActionButton uploadFab = (FloatingActionButton) findViewById(R.id.uploadFab);

/*
        cancelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelFab.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
                saveFab.startAnimation(animTranslate(0.0f, -180.0f, 10, height - 240, saveFab, 100));
            }
        });*/

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1, null);
        view2 = inflater.inflate(R.layout.layout2, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("正面");
        titleList.add("侧面");

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));

                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }

        };

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            //页面跳转完后调用
            public void onPageSelected(int arg0) {
                //获取当前页面
                page = arg0;
            }

            @Override
            //页面滑动时调用
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            //状态改变时调用
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        //编辑按钮监听器
        editFab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(0 == page){
                    //img1=(ImageView)findViewById(R.id.img1);
                    photoView = (PhotoView)findViewById(R.id.img1);
                    //BitmapDrawable bitmapDrawable1 = (BitmapDrawable)img1.getDrawable();
                    /*
                    if(bitmapDrawable1 != null) {
                        Bundle bundle = new Bundle();
                        Bitmap bitmap = bitmapDrawable1.getBitmap();
                        bundle.putParcelable("bitmap",bitmap);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(Pager.this, showImage.class);
                        startActivity(intent);
                    }
                    //原来无图片时直接调用照相机
                    else {*/
                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            //Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"pic1.jpg"));
                            //intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                            savePath = "mnt/sdcard/TakePhotos/";
                            pic1File = new File(savePath);
                            if (!pic1File.exists()) {
                                //pic1File.createNewFile();
                                pic1File.mkdir();
                            }
                            String pic1Path=savePath+"pic1.jpg";
                            File file = new File(pic1Path);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, PHOTO_1);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    //}
                }else{
                    img2=(ImageView)findViewById(R.id.img2);
                    //BitmapDrawable bitmapDrawable2 = (BitmapDrawable)img2.getDrawable();

                    /*
                    if(bitmapDrawable2 != null) {
                        Bundle bundle = new Bundle();
                        Bitmap bitmap = bitmapDrawable2.getBitmap();
                        bundle.putParcelable("bitmap",bitmap);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(Pager.this, showImage.class);
                        startActivity(intent);
                    }
                    //原来无图片时直接调用照相机
                    else {
*/
                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            //Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"pic1.jpg"));
                            //intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                            savePath = "mnt/sdcard/TakePhotos/";
                            pic2File = new File(savePath);
                            if (!pic2File.exists()) {
                                pic2File.mkdir();
                            }
                            String pic2Path=savePath+"pic2.jpg";
                            File file = new File(pic2Path);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, PHOTO_2);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    //}
                }
            }
        });

        //确定照片
        uploadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(RESULT_CANCELED == resultCode){
            return;
        }
        //获取相机数据填入第一个ImageView
        if ( PHOTO_1 == requestCode ) {
            try {
                //获取文件输入流
                FileInputStream file = new FileInputStream("mnt/sdcard/TakePhotos/pic1.jpg");
                BitmapFactory.Options opts = new BitmapFactory.Options();
                //为位图设置100K的缓存
                opts.inTempStorage = new byte[100*1024];
                //设置位图颜色显示优化方式
                opts.inPreferredConfig=Bitmap.Config.RGB_565;
                //设置图片可以被回收
                opts.inPurgeable=true;
                //设置位图缩放比例
                opts.inSampleSize=10;
                //设置解码位图的尺寸信息
                opts.inInputShareable=true;
                //解码位图
                bitmap1=BitmapFactory.decodeStream(file,null,opts);
                //设置ImageView背景
                //img1.setBackgroundColor(Color.BLACK);
                photoView.setBackgroundColor(Color.BLACK);
                //显示位图
                //img1.setImageBitmap(bitmap1);
                OtsuBinaryFilter filter = new OtsuBinaryFilter();
                Bitmap bit =null;
                bit = filter.edge(bitmap1);

                photoView.setImageBitmap(bit);
                
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //获取相机数据填入第二个ImageView
        else if( PHOTO_2 == requestCode ){
            try {
                //获取文件输入流
                FileInputStream file = new FileInputStream("mnt/sdcard/TakePhotos/pic2.jpg");
                BitmapFactory.Options opts = new BitmapFactory.Options();
                //为位图设置100K的缓存
                opts.inTempStorage = new byte[100*1024];
                //设置位图颜色显示优化方式
                opts.inPreferredConfig=Bitmap.Config.RGB_565;
                //设置图片可以被回收
                opts.inPurgeable=true;
                //设置位图缩放比例
                opts.inSampleSize=10;
                //设置解码位图的尺寸信息
                opts.inInputShareable=true;
                //解码位图
                bitmap2=BitmapFactory.decodeStream(file,null,opts);
                //设置ImageView背景
                img2.setBackgroundColor(Color.BLACK);
                //显示位图
                img2.setImageBitmap(bitmap2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




    //按钮特效动画
    /*
    protected Animation setAnimScale(float toX, float toY)
    {
        // TODO Auto-generated method stub
        animationScale = new ScaleAnimation(1f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
        animationScale.setInterpolator(Pager.this, android.R.anim.accelerate_decelerate_interpolator);
        animationScale.setDuration(500);
        animationScale.setFillAfter(false);
        return animationScale;

    }

    protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue)
    {
        // TODO Auto-generated method stub
        animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        animationRotate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                animationRotate.setFillAfter(true);
            }
        });
        return animationRotate;
    }

    protected Animation animTranslate(float toX, float toY, final int lastX, final int lastY,
                                      final FloatingActionButton button, long durationMillis)
    {
        // TODO Auto-generated method stub
        animationTranslate = new TranslateAnimation(0, toX, 0, toY);
        animationTranslate.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                // TODO Auto-generated method stub
                //params = new RelativeLayout.LayoutParams(0, 0);
                params = new android.support.design.widget.CoordinatorLayout.LayoutParams(0, 0);
                params.height = 50;
                params.width = 50;
                params.setMargins(lastX, lastY, 0, 0);
                button.setLayoutParams(params);
                button.clearAnimation();

            }
        });
        animationTranslate.setDuration(durationMillis);
        return animationTranslate;
    }*/
}

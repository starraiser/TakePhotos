package com.example.administrator.takephotos;

/**
 * @author  star
 * @date 2015.12
 */
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.takephotos.R;

public class Pager extends Activity {

    private Animation animationTranslate, animationRotate, animationScale;
    private static int width, height;
    //private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
    private android.support.design.widget.CoordinatorLayout.LayoutParams params = new android.support.design.widget.CoordinatorLayout.LayoutParams(0, 0);
    private static Boolean isClick = false;

    private View view1, view2, view3;
    private List<View> viewList;// view数组
    private ViewPager viewPager; // 对应的viewPager
    private int page;

    private ImageView img1;
    private ImageView img2;

    private String savePath;

    private File pic1File;
    private File pic2File;

    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.editFab);
        //final FloatingActionButton cancelFab = (FloatingActionButton) findViewById(R.id.cancelFab);
        final FloatingActionButton saveFab = (FloatingActionButton) findViewById(R.id.uploadFab);
        //editFab.setVisibility(View.INVISIBLE);
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
                    img1=(ImageView)findViewById(R.id.img1);
                    BitmapDrawable bitmapDrawable1 = (BitmapDrawable)img1.getDrawable();
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
                            System.out.println(pic1Path);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, 1);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    //}
                }else{
                    img2=(ImageView)findViewById(R.id.img2);
                    BitmapDrawable bitmapDrawable2 = (BitmapDrawable)img2.getDrawable();

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
                            pic1File = new File(savePath);
                            if (!pic1File.exists()) {
                                //pic1File.createNewFile();
                                pic1File.mkdir();
                            }
                            String pic2Path=savePath+"pic2.jpg";
                            File file = new File(pic2Path);
                            System.out.println(pic2Path);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, 2);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    //}
                }
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
        if ( 1 == requestCode ) {/*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            Bitmap bm = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",options);
            options.inJustDecodeBounds=false;
            int be = (int)(options.outHeight/(float)200);
            if(be<=0)
                be =1;
            options.inSampleSize = be;
            bm = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",options);*/
            try {
                FileInputStream file = new FileInputStream("mnt/sdcard/TakePhotos/pic1.jpg");
                //img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Bitmap bm = BitmapFactory.decodeStream(file);
                img1.setBackgroundColor(Color.BLACK);
                img1.setImageBitmap(bm);
            }catch (Exception e){

            }
            //Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",null);
            //if(null != bitmap){
            //    img1.setImageBitmap(bitmap);
            //}
        }

        //获取相机数据填入第二个ImageView
        else if( 2 == requestCode ){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            Bitmap bm = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",options);
            options.inJustDecodeBounds=false;
            int be = (int)(options.outHeight/(float)200);
            if(be<=0)
                be =1;
            options.inSampleSize = be;
            bm = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic2.jpg",options);
            //img2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img2.setBackgroundColor(Color.BLACK);
            img2.setImageBitmap(bm);
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

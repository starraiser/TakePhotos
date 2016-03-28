package com.example.administrator.takephotos.Activity;

/**
 * @author  star
 * @date 2015.12
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.takephotos.Adapter.BVAdapter;
import com.example.administrator.takephotos.Database.DBManager;
import com.example.administrator.takephotos.Entity.Info;
import com.example.administrator.takephotos.Process.BodyData;
import com.example.administrator.takephotos.Process.ImageProcess;
import com.example.administrator.takephotos.Process.OtsuBinaryFilter;
import com.example.administrator.takephotos.R;
import com.tandong.sa.bv.BottomView;

import uk.co.senab.photoview.PhotoView;

public class Pager extends Activity {
    private final int PHOTO_1 = 1;
    private final int PHOTO_2 = 2;
    private final int UPLOAD = 3;
    private final int SELECT_1 = 4;
    private final int SELECT_2 = 5;

    private boolean hasPic1 = false;
    private boolean hasPic2 = false;

    //private Animation animationTranslate, animationRotate, animationScale;
    //private static int width, height;
    //private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
    //private android.support.design.widget.CoordinatorLayout.LayoutParams params = new android.support.design.widget.CoordinatorLayout.LayoutParams(0, 0);
    //private static Boolean isClick = false;

    private View view1, view2;
    private List<View> viewList;// view数组
    private ViewPager viewPager; // 对应的viewPager
    private int page;
    private int userId;

    //显示图片的ImageView
    private PhotoView img1;
    private PhotoView img2;

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

    private BottomView bottomView;

    private DBManager database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        database = new DBManager(this);

        img1 = (PhotoView)findViewById(R.id.img1);
        img2 = (PhotoView)findViewById(R.id.img2);

        final FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.editFab);
        //final FloatingActionButton cancelFab = (FloatingActionButton) findViewById(R.id.cancelFab);
        final FloatingActionButton uploadFab = (FloatingActionButton) findViewById(R.id.uploadFab);

        SharedPreferences sharedPreferences = getSharedPreferences("info", Activity.MODE_PRIVATE);  // 获取当前用户id
        userId = sharedPreferences.getInt("userId",-1);

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
                    img1 = (PhotoView)findViewById(R.id.img1);
                    //BitmapDrawable bitmapDrawable1 = (BitmapDrawable)img1.getDrawable();

                    ListView choiceList;
                    final ArrayList<String> menus = new ArrayList<String>();
                    menus.add("拍摄照片");
                    menus.add("从文件中选择");

                    bottomView = new BottomView(Pager.this,R.style.BottomViewTheme_Defalut,R.layout.bottom_view);
                    bottomView.setAnimation(R.style.BottomToTopAnim);
                    bottomView.showBottomView(true);

                    choiceList = (ListView)bottomView.getView().findViewById(R.id.lv_list);
                    BVAdapter adapter = new BVAdapter(Pager.this,menus);
                    choiceList.setAdapter(adapter);

                    choiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String s_menu = menus.get(position);
                            if (s_menu.contains("拍摄照片")) {
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
                            } else if (s_menu.contains("从文件中选择")) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent,SELECT_1);
                            }
                            bottomView.dismissBottomView();
                        }
                    });
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
//                        try {
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                            //Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"pic1.jpg"));
//                            //intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                            savePath = "mnt/sdcard/TakePhotos/";
//                            pic1File = new File(savePath);
//                            if (!pic1File.exists()) {
//                                //pic1File.createNewFile();
//                                pic1File.mkdir();
//                            }
//                            String pic1Path=savePath+"pic1.jpg";
//                            File file = new File(pic1Path);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                            startActivityForResult(intent, PHOTO_1);
//                        }catch(Exception e) {
//                            e.printStackTrace();
//                        }
                    //}
                }else{
                    img2=(PhotoView)findViewById(R.id.img2);
                    ListView choiceList;
                    final ArrayList<String> menus = new ArrayList<String>();
                    menus.add("拍摄照片");
                    menus.add("从文件中选择");

                    bottomView = new BottomView(Pager.this,R.style.BottomViewTheme_Defalut,R.layout.bottom_view);
                    bottomView.setAnimation(R.style.BottomToTopAnim);
                    bottomView.showBottomView(true);

                    choiceList = (ListView)bottomView.getView().findViewById(R.id.lv_list);
                    BVAdapter adapter = new BVAdapter(Pager.this,menus);
                    choiceList.setAdapter(adapter);

                    choiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String s_menu = menus.get(position);
                            if (s_menu.contains("拍摄照片")) {
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
                            } else if (s_menu.contains("从文件中选择")) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent,SELECT_2);
                            }
                            bottomView.dismissBottomView();
                        }
                    });
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
//                        try {
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                            //Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"pic1.jpg"));
//                            //intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                            savePath = "mnt/sdcard/TakePhotos/";
//                            pic2File = new File(savePath);
//                            if (!pic2File.exists()) {
//                                pic2File.mkdir();
//                            }
//                            String pic2Path=savePath+"pic2.jpg";
//                            File file = new File(pic2Path);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                            startActivityForResult(intent, PHOTO_2);
//                        }catch(Exception e) {
//                            e.printStackTrace();
//                        }
                    //}
                }
            }
        });

        viewPager.setAdapter(pagerAdapter);
        //确定照片
        uploadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1 = (PhotoView) findViewById(R.id.img1);
                img2 = (PhotoView) findViewById(R.id.img2);
                if (!hasPic1 && !hasPic2) {  // 判断是否有照片
                    Toast toast = Toast.makeText(Pager.this,"未选择照片",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (!hasPic2) {
                    Toast toast = Toast.makeText(Pager.this,"未选择侧面照",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (!hasPic1) {
                    Toast toast = Toast.makeText(Pager.this,"未选择正面照",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                final EditText et = new EditText(Pager.this);  // 弹出身高输入框
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(Pager.this).setTitle("输入身高(cm)").setView(et)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    String height = et.getText().toString();
                                    if(Double.parseDouble(height)>300||Double.parseDouble(height)<50){
                                        Toast toast = Toast.makeText(Pager.this,"请输入正确的身高！",Toast.LENGTH_SHORT);
                                        toast.show();
                                        return;
                                    }
                                    Info info = new Info(Double.parseDouble(height),database.getSexById(userId));
//                                    BitmapDrawable frontDrawable = (BitmapDrawable)img1.getDrawable();
//                                    Bitmap front = frontDrawable.getBitmap();
//                                    BitmapDrawable sideDrawable = (BitmapDrawable)img2.getDrawable();
//                                    Bitmap side = sideDrawable.getBitmap();
                                    OtsuBinaryFilter filter = new OtsuBinaryFilter();
                                    Bitmap front = filter.filter(bitmap1);
                                    Bitmap side = filter.filter(bitmap2);
                                    BodyData data = new BodyData(Float.parseFloat(height),front,side);

                                    info.set_breast(data.getBreastWidth(), data.getBreastThickness());
                                    info.set_waist(data.getWaistWidth(), data.getWaistThickness());
                                    info.set_hipshot(data.getHipshotWidth(), data.getHipshotThickness());
                                    info.set_userId(userId);

                                    Bundle bundle = new Bundle();
                                    bundle.putDouble("height", info.get_height());
                                    bundle.putDouble("waist", info.get_waist());
                                    bundle.putDouble("breast", info.get_breast());
                                    bundle.putDouble("hipshot", info.get_hipshot());
                                    bundle.putString("sex", info.get_sex());
                                    bundle.putInt("userId", userId);

                                    database.addUserInfo(info);

                                    Intent intent = new Intent(Pager.this,showinfo.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("cancel", null).show();
            }
        });

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
                img1.setBackgroundColor(Color.BLACK);
                //显示位图
                //img1.setImageBitmap(bitmap1);
                OtsuBinaryFilter filter = new OtsuBinaryFilter();
                Bitmap bit =null;
                bit = filter.edge(bitmap1);
                ImageProcess process = new ImageProcess();
                bit = process.toGrey(bitmap1);

                hasPic1=true;
                img1.setImageBitmap(bit);
                
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
                OtsuBinaryFilter filter = new OtsuBinaryFilter();
                Bitmap bit =null;
                bit = filter.edge(bitmap2);
                ImageProcess process = new ImageProcess();
                bit = process.toGrey(bitmap2);
                hasPic2=true;
                img2.setImageBitmap(bit);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if(requestCode == SELECT_1){
            img1 = (PhotoView)findViewById(R.id.img1);
            if(data==null){
                return;
            }
            Uri uri = data.getData();
            AssetFileDescriptor afd;
            try{
                afd = getContentResolver().openAssetFileDescriptor(uri,"r");
                byte[] buffer = new byte[16*1024];
                FileInputStream fis = afd.createInputStream();
                savePath = "mnt/sdcard/TakePhotos/";
                String photoPath=savePath + "pic1.jpg";
                System.out.println(photoPath);
                FileOutputStream fos = new FileOutputStream(new File(photoPath));
                ByteArrayOutputStream temp_byte = new ByteArrayOutputStream();
                int size;
                while((size=fis.read(buffer))!=-1){
                    fos.write(buffer,0,size);
                    temp_byte.write(buffer,0,size);
                }
//                ImageLoaderConfiguration config =
//                        ImageLoaderConfiguration.createDefault(getApplicationContext());  // 配置
//                ImageLoader.getInstance().init(config);  // 初始化
//                ImageLoader.getInstance().displayImage("file:///" + photoPath, img1);  // 载入图片
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
                Bitmap bitmap = null;
                OtsuBinaryFilter filter = new OtsuBinaryFilter();
                bitmap = filter.edge(bitmap1);
                img1.setBackgroundColor(Color.BLACK);
                //显示位图
                //img1.setImageBitmap(bitmap1);

                ImageProcess process = new ImageProcess();
                bitmap = process.toGrey(bitmap1);
                hasPic1=true;
                img1.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println(uri);
        } else if(requestCode == SELECT_2){
            img2 = (PhotoView)findViewById(R.id.img2);
            if(data==null){
                return;
            }
            Uri uri = data.getData();
            AssetFileDescriptor afd;
            try{
                afd = getContentResolver().openAssetFileDescriptor(uri,"r");
                byte[] buffer = new byte[16*1024];
                FileInputStream fis = afd.createInputStream();
                savePath = "mnt/sdcard/TakePhotos/";
                String photoPath=savePath + "pic2.jpg";
                System.out.println(photoPath);
                FileOutputStream fos = new FileOutputStream(new File(photoPath));
                ByteArrayOutputStream temp_byte = new ByteArrayOutputStream();
                int size;
                while((size=fis.read(buffer))!=-1){
                    fos.write(buffer,0,size);
                    temp_byte.write(buffer,0,size);
                }
//                ImageLoaderConfiguration config =
//                        ImageLoaderConfiguration.createDefault(getApplicationContext());  // 配置
//                ImageLoader.getInstance().init(config);  // 初始化
//                ImageLoader.getInstance().displayImage("file:///" + photoPath, img1);  // 载入图片
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
                Bitmap bitmap = null;
                OtsuBinaryFilter filter = new OtsuBinaryFilter();
                bitmap = filter.edge(bitmap2);
                //设置ImageView背景
                //img1.setBackgroundColor(Color.BLACK);
                img2.setBackgroundColor(Color.BLACK);
                //显示位图
                //img1.setImageBitmap(bitmap1);

                ImageProcess process = new ImageProcess();
                bitmap = process.toGrey(bitmap1);
                hasPic1=true;
                img2.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            System.out.println(uri);
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

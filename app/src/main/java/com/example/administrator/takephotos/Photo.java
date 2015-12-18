package com.example.administrator.takephotos;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import android.media.ThumbnailUtils;

public class Photo extends AppCompatActivity {

    private ImageView img1;
    private ImageView img2;

    private String savePath;
    private String pic1Path;
    private String pic2Path;

    private File pic1File;
    private File pic2File;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);

        img1=(ImageView)findViewById(R.id.image1);
        img2=(ImageView)findViewById(R.id.image2);

        //final BitmapDrawable bitmapDrawable1 = (BitmapDrawable)img1.getDrawable();
        //final BitmapDrawable bitmapDrawable2 = (BitmapDrawable)img2.getDrawable();

       // Bitmap bitmap = te.getBitmap();
        //final Bitmap bitmap = ((BitmapDrawable)img1.getDrawable()).getBitmap();
        //final Bitmap bitmap2 = ((BitmapDrawable)img2.getDrawable()).getBitmap();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //第一个ImageView的监听器
        img1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                BitmapDrawable bitmapDrawable1 = (BitmapDrawable)img1.getDrawable();

                //判断原来是否有图片，有图片则进入全屏预览
                if(bitmapDrawable1 != null) {
                    Bundle bundle = new Bundle();
                    Bitmap bitmap = bitmapDrawable1.getBitmap();
                    bundle.putParcelable("bitmap",bitmap);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(Photo.this, showImage.class);
                    startActivity(intent);
                }
                //原来无图片时直接调用照相机
                else {

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
                }
            }
        });

        //第二个ImageView的监听器
        img2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                BitmapDrawable bitmapDrawable2 = (BitmapDrawable)img2.getDrawable();
                //判断原来是否有图片，有图片则进入全屏预览
                if(bitmapDrawable2 != null) {
                    Bundle bundle = new Bundle();
                    Bitmap bitmap = bitmapDrawable2.getBitmap();
                    bundle.putParcelable("bitmap",bitmap);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(Photo.this, showImage.class);
                    startActivity(intent);
                }
                //原来无图片时直接调用照相机
                else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //获取相机数据填入第一个ImageView
        if ( 1 == requestCode ) {
            /*
            Bundle bundle=data.getExtras();
            if (bundle!=null) {
                Bitmap bm=(Bitmap) bundle.get("data");

                img1.setLayoutParams(new LinearLayout.LayoutParams(bm.getWidth(),bm.getHeight()));
                img1.setImageBitmap(bm);
            }


            final BitmapDrawable bitmapDrawabletest = (BitmapDrawable)img1.getDrawable();
            if(bitmapDrawabletest!=null){
                System.out.println("12312312312312312312321");
            }*/
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            Bitmap bm = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",options);
            options.inJustDecodeBounds=false;
            int be = (int)(options.outHeight/(float)200);
            if(be<=0)
                be =1;
            options.inSampleSize = be;
            bm = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",options);
            img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img1.setBackgroundColor(Color.BLACK);
            img1.setImageBitmap(bm);
            //Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/TakePhotos/pic1.jpg",null);
            //if(null != bitmap){
            //    img1.setImageBitmap(bitmap);
            //}
        }
        //获取相机数据填入第二个ImageView
        else if( 2 == requestCode ){
            Bundle bundle=data.getExtras();
            if (bundle!=null) {
                Bitmap bm=(Bitmap) bundle.get("data");
                img2.setImageBitmap(bm);
            }
        }
    }

}

package com.example.administrator.takephotos.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.takephotos.ActivityManager.ActivityTaskManager;
import com.example.administrator.takephotos.R;

public class showImage extends AppCompatActivity {

    private ImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ActivityTaskManager.getInstance().putActivity("showImage", this);

        pic = (ImageView)findViewById(R.id.showPic);

        Bundle bundle = getIntent().getExtras();
        Bitmap bitmap = bundle.getParcelable("bitmap");
        pic.setImageBitmap(bitmap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //获取相机数据填入ImageView
        if ( 1 == requestCode ) {
            Bundle bundle=data.getExtras();
            if (bundle!=null) {
                Bitmap bm=(Bitmap) bundle.get("data");
                pic.setImageBitmap(bm);
            }
        }

    }

}

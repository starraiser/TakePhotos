package com.example.administrator.takephotos.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.takephotos.Database.DBManager;
import com.example.administrator.takephotos.Entity.Info;
import com.example.administrator.takephotos.R;

public class showinfo extends Activity {

    private TextView Height;
    private TextView Breast;
    private TextView Waist;
    private TextView Hipshot;

    private DBManager database;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfo);

        database = new DBManager(this);
//        Intent intent = new Intent();
//        Bundle bundle = intent.getExtras();
//        Double height = bundle.getDouble("height");
//        Double breast = bundle.getDouble("breast");
//        Double waist = bundle.getDouble("waist");
//        Double hipshot = bundle.getDouble("hipshot");

        SharedPreferences sharedPreferences = getSharedPreferences("info", Activity.MODE_PRIVATE);  // 获取当前用户id
        userId = sharedPreferences.getInt("userId",-1);

        Info info = database.getInfoById(userId);
        Double height = info.get_height();
        Double breast = info.get_breast();
        Double waist = info.get_waist();
        Double hipshot = info.get_hipshot();


        Height = (TextView)findViewById(R.id.height);
        Breast = (TextView)findViewById(R.id.breast);
        Waist = (TextView)findViewById(R.id.waist);
        Hipshot = (TextView)findViewById(R.id.hipshot);

        Height.setText("你的身高："+String.valueOf(height));
        Breast.setText("你的胸围："+String.valueOf(breast));
        Waist.setText("你的腰围："+String.valueOf(waist));
        Hipshot.setText("你的臀围："+String.valueOf(hipshot));
    }
}
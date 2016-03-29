package com.example.administrator.takephotos.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.takephotos.ActivityManager.ActivityTaskManager;
import com.example.administrator.takephotos.Database.DBManager;
import com.example.administrator.takephotos.Entity.Info;
import com.example.administrator.takephotos.Process.Evaluate;
import com.example.administrator.takephotos.R;

public class showinfo extends Activity {

    private TextView Height;
    private TextView Breast;
    private TextView Waist;
    private TextView Hipshot;
    private TextView result;
    private TextView confirm;

    private DBManager database;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfo);
        ActivityTaskManager.getInstance().putActivity("MainActivity", this);

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
        result = (TextView)findViewById(R.id.result);
        confirm = (TextView)findViewById(R.id.confirm);

        Height.setText("你的身高："+String.valueOf(height));
        Breast.setText("你的胸围："+String.valueOf(breast));
        Waist.setText("你的腰围："+String.valueOf(waist));
        Hipshot.setText("你的臀围："+String.valueOf(hipshot));
        Evaluate evaluate = new Evaluate();
        result.setText(evaluate.eva(info));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("231321321321");
                finish();
                ActivityTaskManager.getInstance().closeAllActivityExceptOne("MainActivity");  // 退出到登录页面
            }
        });
    }
}

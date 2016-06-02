package com.example.administrator.takephotos.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.takephotos.ActivityManager.ActivityTaskManager;
import com.example.administrator.takephotos.Database.DBManager;
import com.example.administrator.takephotos.Entity.Info;
import com.example.administrator.takephotos.R;

public class InputData extends Activity {

    private EditText height;
    private EditText breast;
    private EditText waist;
    private EditText hipshot;
    private TextView confirm;

    DBManager database;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        database = new DBManager(this);
        ActivityTaskManager.getInstance().putActivity("InputData", this);

        SharedPreferences sharedPreferences = getSharedPreferences("info", Activity.MODE_PRIVATE);  // 获取当前用户id
        userId = sharedPreferences.getInt("userId",-1);

        height = (EditText)findViewById(R.id.height);
        breast = (EditText)findViewById(R.id.breast);
        waist = (EditText)findViewById(R.id.waist);
        hipshot = (EditText)findViewById(R.id.hipshot);
        confirm = (TextView)findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heightData = height.getText().toString();
                String breastData = breast.getText().toString();
                String waistData = waist.getText().toString();
                String hipshotData = hipshot.getText().toString();
                if(heightData.equals("")||breastData.equals("")||waistData.equals("")||hipshotData.equals("")){
                    Toast toast = Toast.makeText(InputData.this,"请输入完整数据！",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(Double.parseDouble(heightData)>300||Double.parseDouble(heightData)<50){
                    Toast toast = Toast.makeText(InputData.this,"请输入正确的身高！",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Info info = new Info(Double.parseDouble(heightData),database.getSexById(userId));
                info.set_userId(userId);
                info.set_breast(Double.parseDouble(breastData));
                info.set_waist(Double.parseDouble(waistData));
                info.set_hipshot(Double.parseDouble(hipshotData));

                database.addUserInfo(info);

                Bundle bundle = new Bundle();
                bundle.putDouble("height", info.get_height());
                bundle.putDouble("waist", info.get_waist());
                bundle.putDouble("breast", info.get_breast());
                bundle.putDouble("hipshot", info.get_hipshot());
                bundle.putString("sex", info.get_sex());
                bundle.putInt("userId", userId);

                Intent intent = new Intent(InputData.this,showinfo.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }
}

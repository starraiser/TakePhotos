package com.example.administrator.takephotos.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.takephotos.ActivityManager.ActivityTaskManager;
import com.example.administrator.takephotos.R;
import com.example.administrator.takephotos.Database.DBManager;
import com.example.administrator.takephotos.Entity.User;

public class Register extends Activity {

    private EditText userName;
    private EditText password;
    private EditText passwordAgain;
    private TextView confirm;
    private RadioGroup radioGroup;
    private DBManager database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityTaskManager.getInstance().putActivity("Register", this);

        database = new DBManager(this);

        userName = (EditText)findViewById(R.id.usernameReg);
        password = (EditText)findViewById(R.id.passwordReg);
        passwordAgain = (EditText)findViewById(R.id.passwordRegAgain);
        confirm = (TextView)findViewById(R.id.comfirmReg);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempPassword = password.getText().toString();
                String tempPasswordAgain = passwordAgain.getText().toString();
                String tempName = userName.getText().toString();
                if((0 !=tempName.length())&&(0 != tempPasswordAgain.length())
                        &&(0 != tempPassword.length())) {
                    if (tempPassword.equals(tempPasswordAgain)) {
                        if (-1 != database.getIdByName(tempName)) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "用户已存在", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            String sex="";
                            RadioButton buttonMan = (RadioButton)radioGroup.getChildAt(0);
                            RadioButton buttonWoman = (RadioButton)radioGroup.getChildAt(1);
                            if(buttonMan.isChecked()){
                                sex="man";
                            }
                            if(buttonWoman.isChecked()){
                                sex="girl";
                            }
                            if(sex==""){
                                return;
                            }
                            User user = new User(tempName, tempPassword,sex);

                            database.addCache(tempName,tempPassword,0,0);  // 修改缓存的用户名
                            database.addUser(user);  // 向数据库添加新用户

                            SharedPreferences mySharedPreferences =
                                    getSharedPreferences("info", Activity.MODE_PRIVATE);  // 利用SharedPreferences保存当前用户id
                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                            editor.putInt("userId", database.getIdByName(tempName));
                            editor.commit();

                            Intent intentToMain = new Intent();
                            intentToMain.setClass(Register.this, MainActivity.class);
                            startActivity(intentToMain);
                            finish();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "两次密码输入不一致", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "请输入用户名和密码", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        passwordAgain.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    System.out.println(confirm.performClick());
                    return true;
                }
                return false;
            }
        });
    }
}

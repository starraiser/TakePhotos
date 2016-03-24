package com.example.administrator.takephotos.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.takephotos.ActivityManager.ActivityTaskManager;
import com.example.administrator.takephotos.Database.DBManager;
import com.example.administrator.takephotos.Entity.Info;
import com.example.administrator.takephotos.R;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private int userId;
    private TextView welcome;
    private TextView height;
    private TextView breast;
    private TextView waist;
    private TextView hipshot;
    private TextView text;
    DBManager database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = new DBManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityTaskManager.getInstance().putActivity("MainActivity", this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        welcome = (TextView)findViewById(R.id.welcome);
        height = (TextView)findViewById(R.id.height);
        breast = (TextView)findViewById(R.id.breast);
        waist = (TextView)findViewById(R.id.waist);
        hipshot = (TextView)findViewById(R.id.hipshot);
        text = (TextView)findViewById(R.id.text);

        SharedPreferences sharedPreferences = getSharedPreferences("info", Activity.MODE_PRIVATE);  // 获取当前用户id
        userId = sharedPreferences.getInt("userId",-1);

        welcome.setText("欢迎," + database.getNameById(userId));

        if(database.getInfoById(userId)!=null) {
            Info info = database.getInfoById(userId);
            height.setText("身高：" + String.valueOf(info.get_height()));
            breast.setText("胸围：" + String.valueOf(info.get_breast()));
            waist.setText("腰围：" + String.valueOf(info.get_waist()));
            hipshot.setText("臀围：" + String.valueOf(info.get_hipshot()));
        } else {
            height.setVisibility(View.INVISIBLE);
            breast.setVisibility(View.INVISIBLE);
            waist.setVisibility(View.INVISIBLE);
            hipshot.setVisibility(View.INVISIBLE);
            text.setText("无数据");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //Intent intent = new Intent(MainActivity.class,FullscreenActivity.class);
                /*
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Photo.class);
                MainActivity.this.startActivity(intent);*/
                //startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE",TAKE_PICTURE));

                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Pager.class);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,InputData.class);
                startActivity(intent);
            }
        });
        final SlidingMenu menu = new SlidingMenu(this);  // 侧滑菜单
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.35f);
        menu.setBehindWidth(600);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding);

        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {  // 打开侧滑菜单监听
            @Override
            public void onOpened() {

                ListView listView = (ListView) findViewById(R.id.slidingList);  // 侧滑菜单里的列表
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
                        getData2(), R.layout.slidinglist,
                        new String[]{"string"}, new int[]{R.id.func});
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // 列表监听器
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        switch (arg2) {
                            case 0:
                                ActivityTaskManager.getInstance().closeAllActivityExceptOne("Login");  // 退出到登录页面
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event){  // 监听返回键
        if(KeyCode == KeyEvent.KEYCODE_BACK){

            Dialog alertDialog = new AlertDialog.Builder(this).
                    setTitle("Warning")
                    .setMessage("确定要退出吗")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                            ActivityTaskManager.getInstance().closeAllActivity();  // 使用控制类完全退出程序
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            alertDialog.show();
            //Login.temp.finish();  // finish登录页，否则返回键会返回到登录页
            //finish();
            //SysApplication.getInstance().exit();  // 使用控制类完全退出程序
            return false;
        } else if(KeyCode == KeyEvent.KEYCODE_MENU){
//            final SlidingMenu menu = new SlidingMenu(this);  // 侧滑菜单
//            menu.setMode(SlidingMenu.LEFT);
//            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//            menu.setFadeDegree(0.35f);
//            menu.setBehindWidth(600);
//            menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//            menu.setMenu(R.layout.sliding);
//
//            menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {  // 打开侧滑菜单监听
//                @Override
//                public void onOpened() {
//
//                    ListView listView = (ListView) findViewById(R.id.slidingList);  // 侧滑菜单里的列表
//                    SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
//                            getData2(), R.layout.slidinglist,
//                            new String[]{"string"}, new int[]{R.id.func});
//                    listView.setAdapter(adapter);
//
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // 列表监听器
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                            switch (arg2){
//                                case 0:
//                                    ActivityTaskManager.getInstance().closeAllActivityExceptOne("Login");  // 退出到登录页面
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    });
//                }
//            });
        }
        return super.onKeyDown(KeyCode, event);
    }
    private List<Map<String ,String>> getData2(){  // 获取侧滑菜单内容
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        for (int i = 0; i < 1; i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("string","退出");
            list.add(map);
        }
        return list;
    }

    protected void onResume(){  // 重新设置适配器，刷新数据
        super.onResume();
        if(database.getInfoById(userId)!=null) {
            Info info = database.getInfoById(userId);
            height.setVisibility(View.VISIBLE);
            breast.setVisibility(View.VISIBLE);
            waist.setVisibility(View.VISIBLE);
            hipshot.setVisibility(View.VISIBLE);
            text.setText("你的信息：");
            height.setText("身高：" + String.valueOf(info.get_height()));
            breast.setText("胸围：" + String.valueOf(info.get_breast()));
            waist.setText("腰围：" + String.valueOf(info.get_waist()));
            hipshot.setText("臀围：" + String.valueOf(info.get_hipshot()));
        } else {
            height.setVisibility(View.INVISIBLE);
            breast.setVisibility(View.INVISIBLE);
            waist.setVisibility(View.INVISIBLE);
            hipshot.setVisibility(View.INVISIBLE);
            text.setText("无数据");
        }
    }
}

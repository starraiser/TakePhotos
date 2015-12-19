package com.example.administrator.takephotos;

/**
 * @author  harvic
 * @date 2014.8.13
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.takephotos.R;

public class Pager extends Activity {

    private View view1, view2, view3;
    private List<View> viewList;// view数组
    private ViewPager viewPager; // 对应的viewPager
    private int page;

    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);

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
            public void onPageSelected(int arg0) {
                page = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        //System.out.println(pagerAdapter.getItemPosition("正面"));

        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(0 == page){

                }else{

                }
            }
        });

        viewPager.setAdapter(pagerAdapter);

    }

}

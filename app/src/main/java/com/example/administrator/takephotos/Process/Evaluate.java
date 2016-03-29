package com.example.administrator.takephotos.Process;

import com.example.administrator.takephotos.Entity.Info;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Evaluate {
    public String eva(Info info){
        String sex = info.get_sex();
        double height = info.get_height();
        double standardBreast = 0;
        double standardWaist = 0;
        double standardHipshot = 0;
        if(sex.equals("man")){
            standardBreast = height*0.48;
            standardWaist = height*0.47;
            standardHipshot = height*0.51;
        } else{
            standardBreast = height*0.535;
            standardWaist = height*0.365;
            standardHipshot = height*0.565;
        }
        double disBreast = Math.abs(standardBreast-info.get_breast());
        double disWaist = Math.abs(standardWaist - info.get_waist());
        double disHipshot = Math.abs(standardHipshot - info.get_hipshot());
        if(disBreast<=3&&disWaist<=3&&disHipshot<=3){
            System.out.println("healthy");
            return "你很健康！请继续保持！";
        } else {
            if(((standardWaist - info.get_waist())<-3)||((standardBreast - info.get_breast())<-3)||((standardHipshot - info.get_hipshot())<-3)){
                return "体型有点偏胖，注意饮食均衡哦！";
            } else {
                return "太瘦了，一定要补充营养哦！";
            }
        }
    }
}

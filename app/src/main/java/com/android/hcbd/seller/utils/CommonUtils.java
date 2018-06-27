package com.android.hcbd.seller.utils;

/**
 * Created by guocheng on 2018/3/12.
 */

public class CommonUtils {

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}

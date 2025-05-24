package com.www.zz.demojedis.utils;


public class ChineseUtil {

    public static boolean hasChinese(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 0x4e00 && c <= 0x9fbb) {
                return true;
            }
        }
        return false;
    }
}

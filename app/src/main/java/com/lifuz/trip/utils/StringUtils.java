package com.lifuz.trip.utils;

/**
 *
 * String 字符串的工具类
 *
 * 作者：李富
 * 邮箱：lifuzz@163.com
 * 时间：2016/10/21 16:50
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     * @param str 检测的字符串
     * @return 如何为空 返回 true 反之 false
     */
    public static boolean isEmpty(String str) {
        if (null != str && !"".equals(str)) {
            return false;
        } else {
            return true;
        }
    }

}

package com.yy.live.utils;

/**
 * Created by yuanxiaoming on 16/8/23.
 */
public class IntToLongUtils {
    public static long toUnsignedLong(int value){
        if (value > 0) {
            return (long) value;
        } else {
            return value & 0xffffffffL;
        }
    }
}

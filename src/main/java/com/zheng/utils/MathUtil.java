package com.zheng.utils;

import java.math.BigDecimal;

/**
 * 常用数字工具
 * Created by zhenglian on 2016/11/1.
 */
public class MathUtil {

    /**
     * 将 number保留scale小数
     * @param number
     * @param scale
     * @return
     */
    public static double decimal(double number, int scale) {
        BigDecimal b = new BigDecimal(number);
        double decimal = b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return decimal;
    }

}

package com.zheng.utils;

import java.util.UUID;

/** 唯一序列
 * Created by zhenglian on 2016/11/5.
 */
public class OnlyIdBuilder {
    public static String build() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

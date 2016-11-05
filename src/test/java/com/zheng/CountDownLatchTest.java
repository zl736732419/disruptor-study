package com.zheng;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhenglian on 2016/11/5.
 */
public class CountDownLatchTest {
    @Test
    public void test() throws Exception {
        CountDownLatch latch = new CountDownLatch(5);
        System.out.println(latch.getCount());
        latch.countDown();
        System.out.println(latch.getCount());
    }
}

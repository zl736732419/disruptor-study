package com.zheng;

import com.google.common.collect.Maps;
import com.zheng.cache.MemoryCache;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * 测试内存缓存多线程并发情况
 * Created by zhenglian on 2016/11/1.
 */
public class MemoryCacheTest {

    @Test
    public void test() throws Exception {
        final ConcurrentMap map = Maps.newConcurrentMap();
        final Object value = new Object();
        final CountDownLatch latch = new CountDownLatch(100);
        for(int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                public void run() {
                    map.put(MemoryCache.getInstance(), value);
                    latch.countDown();
                }
            }).start();
        }

        latch.await();

        System.out.println(map.keySet().size());
        for(Object key : map.keySet()) {
            System.out.println(key);
        }
    }


}

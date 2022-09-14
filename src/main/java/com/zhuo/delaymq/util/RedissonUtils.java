package com.zhuo.delaymq.util;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redisson工具类
 */
public class RedissonUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedissonUtils.class);

    private static RedissonClient redissonClient;

    // 初始化redissonClient
    static {
        try {
            Config config = Config.fromYAML(RedissonUtils.class.getClassLoader().getResource("redis.yaml"));
            long startTime = System.currentTimeMillis();
            redissonClient = Redisson.create(config);
            long endTime = System.currentTimeMillis();
            logger.info(" initialization RedissonClient use {}ms", endTime-startTime);
            logger.info(" redisConfig:{} ", config.toYAML());
        }catch (Exception e){
            logger.error(" initialization RedissonClient error: ", e);
        }

    }

    // 私有化构造器
    private RedissonUtils(){}



    /**
     * 获取redissonClient
     */
    public static RedissonClient getRedissonClient(){
        return redissonClient;
    }


    /**
     * 关闭redissonClient
     */
    public static void close(){
        redissonClient.shutdown();
    }

    /**
     * 通用对象桶
     * Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象*
     */
    public static <V> RBucket<V> getBucket(String objectName){
        return redissonClient.getBucket(objectName);
    }

    /**
     * 获取map对象
     */
    public static <K,V> RMap<K,V> getMap(String objectName){
        return redissonClient.getMap(objectName);
    }

    /**
     * 获取支持单个元素过期的map对象
     */
    public static <K,V> RMapCache<K,V> getMapCache(String objectName){
        return redissonClient.getMapCache(objectName);
    }

    /**
     * 获取set对象
     */
    public static <V> RSet<V> getSet(String objectName){
        return redissonClient.getSet(objectName);
    }

    /**
     * 获取SortedSet对象
     */
    public static <V> RSortedSet<V> getSortedSet(String objectName){
        return redissonClient.getSortedSet(objectName);
    }

    /**
     * 获取ScoredSortedSett对象
     */
    public static <V> RScoredSortedSet<V> getScoredSortedSet(String objectName) {
        return redissonClient.getScoredSortedSet(objectName);
    }

    /**
     * 获取list对象
     */
    public static <V> RList<V> getList(String objectName){
        return redissonClient.getList(objectName);
    }

    /**
     * 获取queue对象
     */
    public static <V> RQueue<V> getQueue(String objectName){
        return redissonClient.getQueue(objectName);
    }


    /**
     * 获取阻塞队列
     */
    public static <V> RBlockingQueue<V> getBlockingQueue(String objectName){
        return redissonClient.getBlockingQueue(objectName);
    }

    /**
     * Get atomic long
     */
    public static RAtomicLong getAtomicLong(String objectName){
        return redissonClient.getAtomicLong(objectName);
    }

    /**
     * Get lock.
     */
    public static RLock getLock(String objectName){
        return redissonClient.getLock(objectName);
    }
}

package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RScoredSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个有序队列, 其元素为Job的Id, 队列按delayTime排序. 使用redis的SortedSet实现
 */
public class DelayBucket {

    private static final Logger logger = LoggerFactory.getLogger(DelayBucket.class);

    private final String name;
    private final int bucketNumber;

    public DelayBucket(String name, int bucketNumber) {
        this.name = name;
        this.bucketNumber = bucketNumber;
    }

    public String getName() {
        return name;
    }

    public int getBucketNumber() {
        return bucketNumber;
    }


    /**
     * 根据jobId计算其应该放置的bucket的key
     */
    private long computeBucketKey(long jodId) {
        return Math.floorMod(jodId, bucketNumber);
    }

    /**
     * 添加jobId到指定的bucket中
     */
    public void add(Long jobId, Long delayTime) {
        String bucketId = name + ":" + computeBucketKey(jobId);
        logger.info(String.format("add job<%s> to bucket<%s>", jobId, bucketId));
        RedissonUtils.getScoredSortedSet(bucketId).add(delayTime, jobId);
    }

    /**
     * 从指定的bucket中获取delayTime最小的jodId
     */
    public Long getFirstJobId(int bucketKey) {
        String bucketId = name + ":" + bucketKey;
        RScoredSortedSet<Long> scoredSortedSet = RedissonUtils.getScoredSortedSet(name + ":" + bucketKey);
        if (scoredSortedSet.size() == 0) {
            logger.error(String.format("bucket<%s> is empty", bucketId));
            return null;
        }
        Long result = scoredSortedSet.first();
        logger.info(String.format("get job<%s> from bucket<%s>", result.toString(), bucketId));
        return result;
    }


    /**
     * 移除给定的jodId
     */
    public void remove(Long jobId) {
        String bucketId = name + ":" + computeBucketKey(jobId);
        logger.info(String.format("remove job<%s> to bucket<%s>", jobId, bucketId));
        RedissonUtils.getScoredSortedSet(bucketId).remove(jobId);
    }
}

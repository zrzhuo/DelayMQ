package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RScoredSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个有序队列, 其元素为JobItem对象, 队列按delayTime排序. 使用redis的SortedSet实现
 */
public class DelayBucket {

    private static final Logger logger = LoggerFactory.getLogger(DelayBucket.class);

    private final String name;

    public DelayBucket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 添加jobItem到指定的bucket中
     */
    public void addToBucket(String bucketKey, JobItem jobItem) {
        RScoredSortedSet<JobItem> scoredSortedSet = RedissonUtils.getScoredSortedSet(name + ":" + bucketKey);
        scoredSortedSet.add(jobItem.getDelayTime(), jobItem);
    }

    /**
     * 从指定的bucket中获取delayTime最小的jodItem
     */
    public JobItem getFromBucket(String bucketKey) {
        RScoredSortedSet<JobItem> scoredSortedSet = RedissonUtils.getScoredSortedSet(name + ":" + bucketKey);
        if (scoredSortedSet.size() == 0) {
            return null;
        }
        return scoredSortedSet.first();
    }

    /**
     * 从指定的bucket中移除给定的jodItem
     */
    public void removeFromBucket(String bucketKey, JobItem jobItem) {
        RScoredSortedSet<JobItem> scoredSortedSet = RedissonUtils.getScoredSortedSet(name + ":" + bucketKey);
        scoredSortedSet.remove(jobItem);
    }
}

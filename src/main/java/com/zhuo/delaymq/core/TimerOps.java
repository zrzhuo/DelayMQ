package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;

public class TimerOps {

    /**
     * 向任务池中添加Jod
     */
    public static void addJod(String name, Job job) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name + ":JobPool");
        rMap.put(job.getId(), job);
    }

    /**
     * 根据jobId查询Jod
     */
    public static Job getJob(String name, long jodId) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name + ":JobPool");
        return rMap.get(jodId);
    }

    /**
     * 根据jobId删除Jod
     */
    public static void removeJob(String name, long jodId) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name + ":JobPool");
        rMap.remove(jodId);
    }

    /**
     * 添加jobItem到指定的bucket中
     */
    public static void addToBucket(String bucket, JobItem jobItem) {
        RScoredSortedSet<JobItem> scoredSortedSet = RedissonUtils.getScoredSortedSet(bucket);
        scoredSortedSet.add(jobItem.getDelayTime(), jobItem);
    }

    /**
     * 从指定的bucket中获取delayTime最小的jodItem
     */
    public static JobItem getFromBucket(String bucket) {
        RScoredSortedSet<JobItem> scoredSortedSet = RedissonUtils.getScoredSortedSet(bucket);
        if (scoredSortedSet.size() == 0) {
            return null;
        }
        return scoredSortedSet.first();
    }

    /**
     * 从指定的bucket中移除给定的jodItem
     */
    public static void removeFromBucket(String bucket, JobItem jobItem) {
        RScoredSortedSet<JobItem> scoredSortedSet = RedissonUtils.getScoredSortedSet(bucket);
        scoredSortedSet.remove(jobItem);
    }

    /**
     * 将jobId添加到指定topic的队尾
     */
    public static void pushToReadyQueue(String name, String topic, long jobId) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(name + ":" + topic);
        rBlockingQueue.offer(jobId);
    }

    /**
     * 从指定topic的队头中获取jobId
     */
    public static Long pollFromReadyQueue(String name, String topic) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(name + ":" + topic);
        return rBlockingQueue.poll();
    }

}

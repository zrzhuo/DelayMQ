package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务池, 用于存放所有任务的元信息(Job). 使用redis的hash表实现. key为jobId，value为Job
 */
public class JobPool {
    private static final Logger logger = LoggerFactory.getLogger(JobPool.class);

    /**
     * 任务池标识
     */
    private final String name;

    public JobPool(String name) {
        this.name = name;
    }

    /**
     * 向任务池中添加Jod
     */
    public void addJob(Job job) {
        logger.info(String.format("add job<%s> to jobPool<%s>", job, name));
        RMap<Long, Job> rMap = RedissonUtils.getMap(name);
        rMap.put(job.getId(), job);
    }

    /**
     * 根据jobId查询Jod
     */
    public Job getJob(long jodId) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name);
        Job job = rMap.get(jodId);
        logger.info(String.format("get job<%s> from jobPool<%s>", job, name));
        return job;
    }

    /**
     * 根据jobId删除Jod
     */
    public void removeJob(long jodId) {
        logger.info(String.format("add job<id=%d> to jobPool<%s>", jodId, name));
        RMap<Long, Job> rMap = RedissonUtils.getMap(name);
        rMap.remove(jodId);
    }

    /**
     * 更新给定job的delayTime
     */
    public void update(Long jobId, long newDelayTime) {
        Job job = getJob(jobId);
        job.setDelayTime(newDelayTime);
        addJob(job);
        logger.info(String.format("update job<%s> from jobPool<%s>", job, name));
    }
}

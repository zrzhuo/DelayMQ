package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RMap;

/**
 * 任务池, 用于存放所有任务的元信息(Job). 使用redis的hash表实现
 */
public class JobPool {
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
    public void addJod(Job job) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name);
        rMap.put(job.getId(), job);
    }

    /**
     * 根据jobId查询Jod
     */
    public Job getJob(long jodId) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name);
        return rMap.get(jodId);
    }

    /**
     * 根据jobId删除Jod
     */
    public void removeJob(long jodId) {
        RMap<Long, Job> rMap = RedissonUtils.getMap(name);
        rMap.remove(jodId);
    }
}

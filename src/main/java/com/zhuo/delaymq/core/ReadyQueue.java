package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个阻塞队列, 存放处于ready状态的job的id. 使用redis的List实现
 */
public class ReadyQueue {
    private static final Logger logger = LoggerFactory.getLogger(ReadyQueue.class);

    /**
     * readyQueue的标识
     */
    private final String name;

    public ReadyQueue(String name) {
        this.name = name;
    }

    /**
     * 将jobId添加到指定topic的队尾
     */
    public void push(String topic, Long jobId) {
        logger.info(String.format("push job<%d> to ReadyQueue<%s>", jobId, name));
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(name + ":" + topic);
        rBlockingQueue.offer(jobId);
    }

    /**
     * 从指定topic的队头中获取jobId
     */
    public Long poll(String topic) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(name + ":" + topic);
        Long jobId = rBlockingQueue.poll();
        logger.info(String.format("poll job<%d> from ReadyQueue<%s>", jobId, name));
        return jobId;
    }
}

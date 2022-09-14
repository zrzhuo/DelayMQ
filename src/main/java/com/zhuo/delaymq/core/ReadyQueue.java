package com.zhuo.delaymq.core;

import com.zhuo.delaymq.util.RedissonUtils;
import org.redisson.api.RBlockingQueue;

import javax.naming.Name;

/**
 * 一个阻塞队列, 存放已经过了delayTime而准备好被消费的job的id. 使用redis的List实现
 */
public class ReadyQueue {

    /**
     * readyQueue的表标识
     */
    private String name;

    public ReadyQueue(String name) {
        this.name = name;
    }

    /**
     * 将jobId添加到指定topic的队尾
     */
    public void pushToReadyQueue(String topic, long jobId) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(name + ":" + topic);
        rBlockingQueue.offer(jobId);
    }

    /**
     * 从指定topic的队头中获取jobId
     */
    public Long pollFromReadyQueue(String topic) {
        RBlockingQueue<Long> rBlockingQueue = RedissonUtils.getBlockingQueue(name + ":" + topic);
        return rBlockingQueue.poll();
    }
}

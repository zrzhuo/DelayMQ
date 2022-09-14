package com.zhuo.delaymq.producer;

import com.zhuo.delaymq.core.DelayQueue;
import com.zhuo.delaymq.core.Job;
import com.zhuo.delaymq.util.SnowflakeIdUtil;
import com.zhuo.delaymq.util.SnowflakeIdWorker;

/**
 * 生产者
 */
public class Producer {
    private  SnowflakeIdWorker idWorker;

    private DelayQueue delayQueue;

    public Producer(DelayQueue delayQueue){
        idWorker = SnowflakeIdUtil.getSnowflakeIdWorker();
        this.delayQueue = delayQueue;
    }

    /**
     * 生产一个任务到延迟队列中
     */
    public void product(long delayTime, long ttrTime, String topic, String message){
        Job job = new Job(idWorker.nextId(), delayTime, ttrTime, topic, message);
        delayQueue.push(job);
    }
}

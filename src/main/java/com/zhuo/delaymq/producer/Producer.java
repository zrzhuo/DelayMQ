package com.zhuo.delaymq.producer;

import com.zhuo.delaymq.core.DelayMQ;
import com.zhuo.delaymq.core.Job;
import com.zhuo.delaymq.util.SnowflakeIdUtil;
import com.zhuo.delaymq.util.SnowflakeIdWorker;

/**
 * 生产者
 */
public class Producer {
    private final SnowflakeIdWorker idWorker;

    private final DelayMQ delayMQ;

    public Producer(DelayMQ delayMQ){
        this.idWorker = SnowflakeIdUtil.getSnowflakeIdWorker();
        this.delayMQ = delayMQ;
    }

    /**
     * 生产一个任务到延迟队列中
     */
    public void product(long delayTime, long ttrTime, String topic, String message){
        Job job = new Job(idWorker.nextId(), delayTime, ttrTime, topic, message);
        delayMQ.push(job);
    }
}

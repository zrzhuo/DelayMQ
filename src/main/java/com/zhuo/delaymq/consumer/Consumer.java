package com.zhuo.delaymq.consumer;

import com.zhuo.delaymq.core.DelayMQ;
import com.zhuo.delaymq.core.Job;

/**
 * 消费者
 */
public class Consumer {
    private DelayMQ delayMQ;

    public Consumer(DelayMQ delayMQ) {
        this.delayMQ = delayMQ;
    }

    public DelayMQ getDelayQueue() {
        return delayMQ;
    }

    public void setDelayQueue(DelayMQ delayMQ) {
        this.delayMQ = delayMQ;
    }

    /**
     * 从延迟队列获取一个Job
     */
    public Job consume(String topic){
        return delayMQ.pop(topic);
    }

    /**
     * 终结任务
     */
    public void finishJob(long jobId){
        delayMQ.finish(jobId);
    }
}

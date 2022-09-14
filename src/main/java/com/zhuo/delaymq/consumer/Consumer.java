package com.zhuo.delaymq.consumer;

import com.zhuo.delaymq.core.DelayQueue;
import com.zhuo.delaymq.core.Job;

/**
 * 消费者
 */
public class Consumer {
    private DelayQueue delayQueue;

    public Consumer(DelayQueue delayQueue) {
        this.delayQueue = delayQueue;
    }

    public DelayQueue getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(DelayQueue delayQueue) {
        this.delayQueue = delayQueue;
    }

    /**
     * 从延迟队列获取一个Job
     */
    public Job consume(String topic){
        return delayQueue.pop(topic);
    }

    /**
     * 终结任务
     */
    public void finishJob(long jobId){
        delayQueue.finish(jobId);
    }
}

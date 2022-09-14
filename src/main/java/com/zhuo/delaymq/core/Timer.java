package com.zhuo.delaymq.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 实时扫描指定Bucket，将delayTime大于等于当前时间的Job放入的ReadyQueue中相应的topic下
 */
public class Timer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    private final String bucketKey;

    private final JobPool jobPool;
    private final DelayBucket delayBucket;
    private final ReadyQueue readyQueue;

    public Timer(JobPool jobPool, DelayBucket delayBucket, ReadyQueue readyQueue, String bucketKey) {
        this.jobPool = jobPool;
        this.delayBucket = delayBucket;
        this.readyQueue = readyQueue;
        this.bucketKey = bucketKey;
    }

    /**
     * 阻塞1s
     */
    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1L);
        }catch (InterruptedException e){
            logger.error("error: ",e);
        }
    }

    /**
     * 启动Timer, 监控bucket
     */
    @Override
    public void run() {
        System.out.println("正在监控Bucket: " + delayBucket.getName() + bucketKey);
        while (true) {
            try {
                JobItem jobItem = delayBucket.getFromBucket(this.bucketKey); // 取当前bucket的队头元素
                // 当前bucket为空
                if (jobItem == null) {
                    sleep();
                    continue;
                }
                // 当前时间未达到delayTime
                if (System.currentTimeMillis() < jobItem.getDelayTime()) {
                    sleep();
                    continue;
                }
                Job job = jobPool.getJob(jobItem.getJobId()); // 获取当前jobItem的元信息
                // 元信息不存在与任务池中, 移除该JobItem
                if (job == null) {
                    delayBucket.removeFromBucket(bucketKey, jobItem);
                    continue;
                }
                // 当前时间未达到delayTime
                if (System.currentTimeMillis() < jobItem.getDelayTime()) {
                    // 删除旧的
                    delayBucket.removeFromBucket(bucketKey, jobItem);
                    // 重新计算延迟时间
                    delayBucket.addToBucket(bucketKey, new JobItem(job));
                }
                // 当前时间达到了delayTime
                else {
                    // 加入到ReadyQueue的对应topic下, 并从bucket中移除
                    readyQueue.pushToReadyQueue(job.getTopic(), job.getId());
                    delayBucket.removeFromBucket(bucketKey, jobItem);
                }
            } catch (Exception e) {
                logger.error("扫描delayBucket出错：", e);
            }
        }
    }

}

package com.zhuo.delaymq.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 实时扫描指定Bucket，将delayTime大于等于当前时间的Job的jobId放入到ReadyQueue中相应的topic下
 */
public class Timer {
    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    private final DelayBucket delayBucket;
    private final ReadyQueue readyQueue;
    private final JobPool jobPool;

    public Timer(DelayBucket delayBucket, ReadyQueue readyQueue, JobPool jobPool) {
        this.delayBucket = delayBucket;
        this.readyQueue = readyQueue;
        this.jobPool = jobPool;
    }

    /**
     * 阻塞1s
     */
    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e){
            logger.error("error: ",e);
        }
    }

    /**
     * 启动Timer, 监控bucket
     */
    public void run() {
        for(int i = 0; i < delayBucket.getBucketNumber(); ++i) {
            int  key = i;
            new Thread( () -> {
                listening(key);
            }).start();
        }
    }

    private void listening(int bucketKey) {
        System.out.println("正在监控Bucket: " + delayBucket.getName() + bucketKey);
        while (true) {
            try {
                Long jobId = delayBucket.getFirstJobId(bucketKey); // 取当前bucket的队头元素
                if (jobId == null) {
                    sleep(); // 当前bucket为空, 阻塞
                    continue;
                }
                Job job = jobPool.getJob(jobId); // 获取当前job
                if (job.getDelayTime() > System.currentTimeMillis()) {
                    sleep(); // 当前时间未达到delayTime, 阻塞
                    continue;
                }
                delayBucket.remove(jobId); // 从delayBucket中删除
                readyQueue.push(job.getTopic(), job.getId()); // 加入到ReadyQueue的对应topic下
            } catch (Exception e) {
                logger.error("扫描delayBucket出错：", e);
            }
        }
    }

}

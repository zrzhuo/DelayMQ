package com.zhuo.delaymq.core;

/**
 * 延迟消息队列, 其中包含多个bucket, 每个bucket都是一个有序队列
 */
public class DelayQueue {
    /**
     * 延迟消息队列标识
     */
    private String name;

    /**
     * bucket数量
     */
    private long bucketNumber;

    /**
     * 任务池
     */
    private JobPool jobPool;

    /**
     * delayBucket
     */
    private DelayBucket delayBucket;

    /**
     * ready队列
     */
    private ReadyQueue readyQueue;


    public DelayQueue(String name, long bucketNumber) {
        this.name = name;
        this.bucketNumber = bucketNumber;
        this.jobPool = new JobPool(name + ":JobPool");
        this.delayBucket = new DelayBucket(name + ":DelayBucket");
        this.readyQueue = new ReadyQueue(name + ":ReadyQueue");
    }




    public void startTimers(){
        // 为每个bucket启动Timer
        for(int i = 0; i < bucketNumber; ++i){
            Timer timer = new Timer(jobPool, delayBucket, readyQueue, String.valueOf(i));
            Thread thread = new Thread(timer);
            thread.start();
        }
    }


    /**
     * 根据jobId计算其应该放置的bucket的key
     */
    private String getBucketKey(long jodId) {
        return String.valueOf(Math.floorMod(jodId, bucketNumber));
    }

    /**
     * 添加Job到延迟队列
     */
    public void push(Job job) {
        jobPool.addJod(job); // 添加到任务池
        JobItem item = new JobItem(job);
        String bucketKey = getBucketKey(job.getId()); // 计算bucketKey
        delayBucket.addToBucket(bucketKey, item); // 添加到相应的bucket中
    }

    /**
     * 从ReadyQueue中获取指定topic的Job
     */
    public Job pop(String topic) {
        // 尝试从readyQueue中获取指定topic的一个jobId
        Long jobId = readyQueue.pollFromReadyQueue(topic);
        // 该topic中无jobId
        if (jobId == null) {
            return null;
        }
        // 获取到一个jobId, 然后根据id从任务池中获取到该Job
        else {
            Job job = jobPool.getJob(jobId);
            // JobPool中不存在该Job, 此时该Job已处于deleted状态, 无需处理
            if (job == null) {
                return null;
            }
            // 仍存在于JobPool中, 此时该Job将处于reserved状态
            else {
                long delayTime = job.getDelayTime();
                // 更新该任务的delayTime
                long reDelayTime = System.currentTimeMillis() + job.getTtrTime();  // 计算超时时间
                job.setDelayTime(reDelayTime); // 将该任务的delayTime更新为超时时间
                jobPool.addJod(job); // 在JobPool中更新
                delayBucket.addToBucket(getBucketKey(job.getId()), new JobItem(job)); // 再bucket中更新
                // 返回的Job还是原来的delayTime
                job.setDelayTime(delayTime);
                return job;
            }
        }
    }

    /**
     * 删除延迟队列任务
     */
    public void delete(long jodId) {
        jobPool.removeJob(jodId);
    }

    /**
     * 完成任务, 将该Job从JobPool中移除, 对应的JobItem从Bucket中移除
     */
    public void finish(long jobId) {
        Job job = jobPool.getJob(jobId);
        if (job == null) {
            return;
        }
        jobPool.removeJob(jobId);
        delayBucket.removeFromBucket(getBucketKey(job.getId()), new JobItem(job));
    }
}

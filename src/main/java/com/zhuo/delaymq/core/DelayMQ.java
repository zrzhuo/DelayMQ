package com.zhuo.delaymq.core;

/**
 *
 */
public class DelayMQ {

    private final DelayBucket delayBucket;
    private final ReadyQueue readyQueue;
    private final JobPool jobPool;
    private final Timer timer;


    public DelayMQ(String name, int bucketNumber) {
        this.delayBucket = new DelayBucket(name + ":DelayBucket", bucketNumber);
        this.jobPool = new JobPool(name + ":JobPool");
        this.readyQueue = new ReadyQueue(name + ":ReadyQueue");
        this.timer = new Timer(delayBucket, readyQueue, jobPool);
    }

    public void run(){
        timer.run();
    }


    /**
     * 添加Job到延迟队列
     */
    public void push(Job job) {
        jobPool.addJob(job); // 添加到任务池
        delayBucket.add(job.getId(), job.getDelayTime()); // 添加到相应的bucket中
    }

    /**
     * 从ReadyQueue中获取指定topic的Job
     */
    public Job pop(String topic) {
        Long jobId = readyQueue.poll(topic); // 尝试从readyQueue中获取指定topic的一个jobId
        if (jobId == null) {
            return null; // 该topic中无jobId
        }
        Job job = jobPool.getJob(jobId); // 根据id从任务池中获取到该Job
        if (job == null) {
            // JobPool中不存在该Job, 此时该Job已处于deleted状态, 无需处理
            return null;
        } else {
            // 仍存在于JobPool中, 此时该Job将处于reserved状态
            long newDelayTime = System.currentTimeMillis() + job.getTtrTime();
            jobPool.update(job.getId(), newDelayTime);
            delayBucket.add(job.getId(), newDelayTime);
            return job;
        }
    }

    /**
     * 删除延迟队列任务
     */
    public void delete(long jodId) {
        jobPool.removeJob(jodId);
    }

    /**
     * 完成任务, 将该Job从JobPool和delayBucket中移除
     */
    public void finish(long jobId) {
        Job job = jobPool.getJob(jobId);
        if (job == null) {
            return;
        }
        delayBucket.remove(jobId);
        delete(jobId);
    }
}

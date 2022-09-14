package com.zhuo.delaymq.core;

/**
 *  延迟队列中实际存放的元素, 包含任务id和任务的delayTime, 以delayTime作为redis中SortedSet的score
 */
public class JobItem {

    /**
     * 任务的执行时间, 作为有序队列的score
     */
    private long delayTime;

    /**
     * 任务的唯一标识
     */
    private long jobId;

    public JobItem() {

    }

    public JobItem(Job job){
        this.jobId = job.getId();
        this.delayTime = job.getDelayTime();
    }

    public JobItem(long jobId, long delayTime) {
        this.jobId = jobId;
        this.delayTime = delayTime;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }
    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "JobItem{" +
                "delayTime=" + delayTime +
                ", jobId=" + jobId +
                '}';
    }
}

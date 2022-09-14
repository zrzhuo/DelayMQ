package com.zhuo.delaymq.core;



/**
 * 任务
 */
public class Job {

    /**
     * 任务的唯一标识
     */
    private long id;

    /**
     * 任务的执行时间
     */
    private long delayTime;

    /**
     * 任务的执行超时时间, 单位为毫秒
     */
    private long ttrTime;

    /**
     * 任务类型(具体业务类型)
     */
    private String topic;

    /**
     * 任务的消息内容，用于处理具体业务逻辑
     */
    private String message;

    public Job() {
    }

    public Job(long id, long delayTime, long ttrTime, String topic, String message) {
        this.id = id;
        this.delayTime = delayTime;
        this.ttrTime = ttrTime;
        this.topic = topic;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public long getTtrTime() {
        return ttrTime;
    }

    public void setTtrTime(long ttrTime) {
        this.ttrTime = ttrTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", delayTime=" + delayTime +
                ", ttrTime=" + ttrTime +
                ", message='" + message + '\'' +
                '}';
    }
}

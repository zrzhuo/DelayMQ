package com.zhuo.delaymq.core;



/**
 * 任务
 */
public class Job {

    /**
     * 任务的唯一标识
     */
    private Long id;

    /**
     * 任务的执行时间
     */
    private Long delayTime;

    /**
     * 任务的执行超时时间, 单位为毫秒
     */
    private Long ttrTime;

    /**
     * 任务的消息类型
     */
    private String topic;

    /**
     * 任务的消息内容，用于消费者进行具体业务处理
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

    public Long getId() {
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

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public Long getTtrTime() {
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

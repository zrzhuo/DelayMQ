package com.zhuo.delaymq.util;


public class SnowflakeIdUtil {

    private static final SnowflakeIdWorker snowflakeIdWorker;

    static {
        snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
    }

    private SnowflakeIdUtil(){}

    public static SnowflakeIdWorker getSnowflakeIdWorker(){
        return snowflakeIdWorker;
    }
}

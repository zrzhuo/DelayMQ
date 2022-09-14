package com.zhuo.delaymq;

import com.zhuo.delaymq.core.DelayQueue;

public class StartService {
    public static void main(String[] args) {
        DelayQueue delayQueue = new DelayQueue("DelayQueue", 3);
        delayQueue.startTimers();
    }
}

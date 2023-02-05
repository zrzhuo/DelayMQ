package com.zhuo.delaymq;

import com.zhuo.delaymq.core.DelayMQ;


public class StartService {
    public static void main(String[] args) {
        DelayMQ delayMQ = new DelayMQ("DelayMQ", 3);
        delayMQ.run();
    }
}

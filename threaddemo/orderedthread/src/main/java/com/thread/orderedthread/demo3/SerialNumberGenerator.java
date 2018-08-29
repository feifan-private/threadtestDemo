package com.thread.orderedthread.demo3;

public class SerialNumberGenerator {
    private static volatile int serialNumnber = 0;
    public static synchronized int nextSerialNumber(){
        return serialNumnber++;
    }
}

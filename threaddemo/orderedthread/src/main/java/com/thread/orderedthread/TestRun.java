package com.thread.orderedthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestRun {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        //模拟加入100个线程
        for (int i=0;i<100;i++)
        exec.execute(new ThreadoFirst(i));
        exec.shutdown();
    }
}

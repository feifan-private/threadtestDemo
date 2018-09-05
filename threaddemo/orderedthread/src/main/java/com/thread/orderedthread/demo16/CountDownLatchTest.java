package com.thread.orderedthread.demo16;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class WaitTask implements Runnable{
    private final CountDownLatch latch;
    WaitTask(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            latch.await();
            System.out.println("exec Waiting");
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new WaitTask(latch));
        TimeUnit.SECONDS.sleep(1);
        latch.countDown();
        System.out.println("countDown 1");
        TimeUnit.SECONDS.sleep(1);
        latch.countDown();
        System.out.println("countDown 2");
        exec.shutdownNow();
    }
}

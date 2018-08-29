package com.thread.orderedthread.demo4;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest implements Runnable{
    private AtomicInteger i = new AtomicInteger(0);
    public int getValue(){return i.get();}
    public void evenIncrement(){i.addAndGet(2);}
    public void run() {
        while (true){
            evenIncrement();
        }
    }

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                System.err.println("Abroting");
                System.exit(0);
            }
        },5000);
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomicIntegerTest ai = new AtomicIntegerTest();
        exec.execute(ai);
        while (true){
            int val = ai.getValue();
            if(val%2 != 0){
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}

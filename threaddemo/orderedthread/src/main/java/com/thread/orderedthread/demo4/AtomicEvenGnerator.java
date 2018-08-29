package com.thread.orderedthread.demo4;

import com.thread.orderedthread.demo1.EvenChecker;
import com.thread.orderedthread.demo1.IntGenerator;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicEvenGnerator  extends IntGenerator{
    private AtomicInteger currentEvenValue = new AtomicInteger(0);
    public int next() {
        return currentEvenValue.addAndGet(2);
    }

    public static void main(String[] args) {
        EvenChecker.test(new AtomicEvenGnerator());
    }
}

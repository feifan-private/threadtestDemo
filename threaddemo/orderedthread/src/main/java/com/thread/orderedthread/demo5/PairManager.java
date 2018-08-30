package com.thread.orderedthread.demo5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class PairManager {
    AtomicInteger checkCount = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());
    public synchronized Pair getPair(){
        return new Pair(p.getX(),p.getY());
    }
    protected void store(Pair p){
        storage.add(p);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public abstract void increment();
}
class PairManager1 extends PairManager{
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}
class PairManeger2 extends PairManager{

    public void increment() {
        Pair temp;
        synchronized (this){
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}
class ExplicitPairManager1 extends PairManager{
    private Lock lock = new ReentrantLock();
    public synchronized void increment() {
        lock.lock();
        try{
            p.incrementX();
            p.incrementY();
            store(getPair());
        }finally {
            lock.unlock();
        }
    }
}
class ExplicitPairManager2 extends PairManager{
    private Lock lock = new ReentrantLock();
    public void increment() {
        Pair temp;
        lock.lock();
        try{
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }finally {
            lock.unlock();
        }
        store(temp);
    }
}
class PairManipulator implements Runnable{
    private PairManager pm;
    public PairManipulator(PairManager pm){
        this.pm = pm;
    }
    public void run() {
        while (true){
            pm.increment();
        }
    }

    public String toString() {
        return "Pair: " + pm.getPair() + " checkCounter =" + pm.checkCount.get();
    }
}
class PairChecker implements Runnable{
    private PairManager pm;
    public PairChecker(PairManager pm){
        this.pm = pm;
    }
    public void run() {
        while (true){
            pm.checkCount.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}

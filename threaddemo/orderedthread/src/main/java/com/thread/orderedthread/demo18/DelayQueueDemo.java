package com.thread.orderedthread.demo18;

import sun.security.krb5.internal.PAForUserEnc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class DelayedTask implements Runnable,Delayed{
    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;
    protected static List<DelayedTask> squeence = new ArrayList<DelayedTask>();
    public DelayedTask(int delayInMilliseseconds){
        delta = delayInMilliseseconds;
        trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta,TimeUnit.MILLISECONDS);
        squeence.add(this);
    }
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
        if (trigger < that.trigger) return  -1;
        if (trigger > that.trigger) return  1;
        return 0;
    }
    public void run() {
        System.out.println(this + " ");
    }
    public String toString() {
        return String.format("[%1$-4d]",delta) + "TASK" + id;
    }
    public String summary(){
        return "(" + id + ":" + delta + ")";
    }
    public static class EndSentinel extends DelayedTask{
        private ExecutorService exec;
        public EndSentinel(int delayInMilliseseconds, ExecutorService e) {
            super(delayInMilliseseconds);
            exec = e;
        }

        public void run() {
            for (DelayedTask pt : squeence){
                System.out.println(pt.summary() + "");
            }
            System.out.println(this + " Calling shutdownNow()");
            exec.shutdownNow();
        }
    }
}
class DelayedTaskConsumer implements Runnable{
    private DelayQueue<DelayedTask> q;
    public DelayedTaskConsumer(DelayQueue<DelayedTask> q){
        this.q = q;
    }
    public void run() {
        try {
            while (!Thread.interrupted()){
                q.take().run();
            }
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
        }
        System.out.println("Finised DelayedTaskConsumer");
    }
}
public class DelayQueueDemo {
    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
        for (int i = 0; i < 20; i++){
            queue.put(new DelayedTask(rand.nextInt(5000)));
        }
        queue.add(new DelayedTask.EndSentinel(5000,exec));
        exec.execute(new DelayedTaskConsumer(queue));
    }
}

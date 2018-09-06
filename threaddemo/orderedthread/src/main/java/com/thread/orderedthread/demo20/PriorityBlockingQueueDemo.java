package com.thread.orderedthread.demo20;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

class PrioritizedTask implements Runnable,Comparable<PrioritizedTask>{
    private Random rand = new Random(47);
    private static int counter = 0;
    private final int id = counter++;
    private final int priority;
    protected static List<PrioritizedTask> squence = new ArrayList<PrioritizedTask>();
    PrioritizedTask(int priority) {
        this.priority = priority;
        squence.add(this);
    }
    public int compareTo(PrioritizedTask o) {
        return priority < o.priority ? 1 : (priority > o.priority ? -1 : 0 );
    }
    public void run() {
        try{
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(250));
        } catch (InterruptedException e) {
            System.out.println("Interrupted sleep " + this);
        }
        System.out.println(this);
    }
    public String toString() {
        return String.format("[%1$-3d]",priority) + " Task " + id;
    }
    public String summary(){
        return "(" + id + ":" + priority + ")";
    }
    public static class EndSeninel extends PrioritizedTask{
        private ExecutorService exec;
        EndSeninel(ExecutorService e) {
            super(-1);
            exec = e;
        }
        public void run() {
            for (PrioritizedTask pt : squence){
                System.out.println(pt.summary());
            }
            System.out.println(this  + "calling shutdownNow()");
        }
    }
}
class PrioritizedTaskProducer implements Runnable{
    private Random rand = new Random(47);
    private Queue<Runnable> queue;
    private ExecutorService exec;
    public PrioritizedTaskProducer(Queue<Runnable> queue, ExecutorService exec) {
        this.queue = queue;
        this.exec = exec;
    }
    public void run() {
        for (int i = 0; i < 20; i++){
            queue.add(new PrioritizedTask(rand.nextInt(10)));
            Thread.yield();
        }
        try{
            for (int i = 0; i < 10; i++){
                TimeUnit.MILLISECONDS.sleep(250);
                queue.add(new PrioritizedTask(10));
            }
            for (int i = 0; i < 10; i++){
                queue.add(new PrioritizedTask(i));
            }
            queue.add(new PrioritizedTask.EndSeninel(exec));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finished PriortizedTaskProducer");
    }
}
class PrioritizedTaskConsumer implements Runnable{
    private PriorityBlockingQueue<Runnable> q;
    public PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> q) {
        this.q = q;
    }
    public void run() {
        try{
            while (!Thread.interrupted()){
                q.take().run();
            }
        } catch (InterruptedException e) {
            System.out.println("Interruped");
        }
        System.out.println("Finished PrioritizedTaskConsumer");
    }
}
public class PriorityBlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();
        exec.execute(new PrioritizedTaskProducer(queue,exec));
        TimeUnit.SECONDS.sleep(5);
        exec.execute(new PrioritizedTaskConsumer(queue));
    }
}

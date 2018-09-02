package com.thread.orderedthread.Demo13;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class Toast{
    Toast(int id) {
        this.id = id;
    }
    public enum Status{ DRY, BUTTERED, JAMMED }
    private Status status = Status.DRY;
    private final int id;
    public void butter(){
        status = Status.BUTTERED;
    }
    public void jam(){
        status = Status.JAMMED;
    }
    public Status getStatus(){
        return status;
    }
    public int getId(){
        return id;
    }
    public String toString() {
        return "Toast " + id + ": " + status;
    }
}
class ToastQueue extends LinkedBlockingDeque<Toast>{}

class Toaster implements Runnable{
    private ToastQueue toastQueue;
    private int count = 0;
    private Random rand = new Random(47);
    public Toaster(ToastQueue tq){
        toastQueue = tq;
    }
    public void run() {
        try {
            while (!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                Toast t = new Toast(count++);
                System.out.println(t);
                toastQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}
class Butterer implements Runnable{
    private ToastQueue  dryQueue,butteredQueue;
    public Butterer(ToastQueue dryQueue, ToastQueue butteredQueue) {
        this.dryQueue = dryQueue;
        this.butteredQueue = butteredQueue;
    }
    public void run() {
        try{
            while (!Thread.interrupted()){
               Toast t = dryQueue.take();
               t.butter();
               System.out.println(t);
               butteredQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Buttered interrupted");
        }
        System.out.println("Butterer off");
    }
}
class Jammer implements Runnable{
    private ToastQueue butteredQueue,finishedQueue;
    public Jammer(ToastQueue butteredQueue, ToastQueue finishedQueue) {
        this.butteredQueue = butteredQueue;
        this.finishedQueue = finishedQueue;
    }
    public void run() {
        try{
            while (!Thread.interrupted()){
               Toast t = butteredQueue.take();
               t.jam();
               System.out.println(t);
               finishedQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Jammed interrupted");
        }
        System.out.println("Jammed off");
    }
}
class Eater implements Runnable{
    private ToastQueue finishedQueue;
    private int counter = 0;
    public Eater(ToastQueue finishedQueue) {
        this.finishedQueue = finishedQueue;
    }
    public void run() {
        try {
            while (!Thread.interrupted()){
                Toast t = finishedQueue.take();
                if(t.getId() != counter++ || t.getStatus() != Toast.Status.JAMMED){
                    System.out.println(">>>>Error: " + t);
                    System.exit(0);
                }else {
                    System.out.println("Chomp! " + t);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}
public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dryQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Butterer(dryQueue,butteredQueue));
        exec.execute(new Jammer(butteredQueue,finishedQueue));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}

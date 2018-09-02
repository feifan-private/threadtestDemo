package com.thread.orderedthread.Demo12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

class LiftOffRunner implements Runnable{
    private BlockingQueue<LiftOff> rockets;
    public LiftOffRunner(BlockingQueue<LiftOff> queue){
        rockets = queue;
    }
    public void add(LiftOff lo){
        try {
            rockets.put(lo);
        } catch (InterruptedException e) {
            System.out.println("interrupted during put()");
        }
    }
    public void run() {
        try {
            while (!Thread.interrupted()){
                LiftOff rocket = rockets.take();
                rocket.run();
            }
        } catch (InterruptedException e) {
            System.out.println("Wait from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}
public class TestBlockingQueues {
    static void getKey(){
        try{
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void getKey(String message){
        System.out.println(message);
        getKey();
    }
    static void test(String msg,BlockingQueue<LiftOff> queue){
        System.out.println(msg);
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++){
            runner.add(new LiftOff(5));
        }
        getKey("press 'Enter' (" + msg +")");
        t.interrupt();
        System.out.println("Finised " + msg + "test");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue",new LinkedBlockingDeque<LiftOff>());
        test("ArrayBlockingQueue",new ArrayBlockingQueue<LiftOff>(3));
        test("SyncronousQueue",new SynchronousQueue<LiftOff>());
    }
}

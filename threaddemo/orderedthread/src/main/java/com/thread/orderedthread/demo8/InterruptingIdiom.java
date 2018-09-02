package com.thread.orderedthread.demo8;

import java.util.concurrent.TimeUnit;

class NeedCleanup{
    private final int id;
    NeedCleanup(int id) {
        this.id = id;
        System.out.println("NeedCleanup " + id);
    }
    public void cleanup(){
        System.out.println("Cleaning Up " + id);
    }
}
class Blocked3 implements Runnable{
    private volatile double d = 0.0;
    public void run() {
        try {
            while (!Thread.interrupted()){
                NeedCleanup n1 = new NeedCleanup(1);
                try {
                    System.out.println("Slepping ");
                    TimeUnit.SECONDS.sleep(1);
                    NeedCleanup n2 = new NeedCleanup(2);
                    try {
                        System.out.println("Calculating");
                        for (int i = 1; i < 2500000; i++){
                            d = d + (Math.PI + Math.E) / d;
                        }
                    }finally {
                        n2.cleanup();
                    }
                }  finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        }catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException");
        }
    }
}
public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
//        if (args.length != 1){
//            System.out.println("usage : java Interrpingidiom delay-in-ms");
//            System.exit(1);
//        }
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(new Integer(5000));
        t.interrupt();
    }
}

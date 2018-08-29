package com.thread.orderedthread.demo3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SerialNumberChecker {
    public static final int SIZE = 10;
    private static CirculSet serials = new CirculSet(1000);
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static class SerialChecker implements Runnable{
        public void run() {
            while (true){
                int serial = SerialNumberGenerator.nextSerialNumber();
                if (serials.contains(serial)){
                    System.out.println("Duplicate :" + serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < SIZE; i++){
            exec.execute(new SerialChecker());
            if(args.length > 0){
                TimeUnit.SECONDS.sleep(new Integer(args[0]));
                System.out.println("No duplicate detected");
                System.exit(0);
            }
        }
    }
}

package com.thread.orderedthread.demo22;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class CheckOutTask<T> implements Runnable{
    private static int count = 0;
    private final int id = count++;
    private Pool<T> pool;
    public CheckOutTask(Pool<T> pool) {
        this.pool = pool;
    }
    public void run() {
        try{
            T item = pool.checkOut();
            System.out.println(this + "checked out " + item);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(this + "checking in " + item);
            pool.checkIn(item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        return "CheckoutTask " + id + " ";
    }
}
public class SemaphoreDemo {
    final static int SIZE = 25;

    public static void main(String[] args) throws InterruptedException {
        final Pool<Fat> pool = new Pool<Fat>(Fat.class,SIZE); //pool中穿件25个Fat
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < SIZE; i++){
            exec.execute(new CheckOutTask<Fat>(pool));//25次去取出再放入
        }
        System.out.println("All CheckoutTask Created");
        List<Fat> list = new ArrayList<Fat>();
        for (int i = 0; i < SIZE; i++){
            Fat f = pool.checkOut();
            System.out.println(i + ": main() thread checked out ");
            f.operation();
            list.add(f);
        }
        Future<?> blocked = exec.submit(new Runnable() {
            public void run() {
                try {
                    pool.checkOut();
                } catch (InterruptedException e) {
                    System.out.println("checkOut() Interrupted");
                }
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("Checking in objects in " + list + list.size());
        for (Fat f : list){
            pool.checkIn(f);
//            System.out.println("checkIn1   " + f);
        }
        for (Fat f : list){
            pool.checkIn(f);
//            System.out.println("checkIn2   " + f);
        }
        exec.shutdown();;
    }
}

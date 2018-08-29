package com.thread.orderedthread;

public class ThreadSecond implements Runnable{
    private int orderNumer;
    ThreadSecond(int orderNumer){
        this.orderNumer = orderNumer;
    }
    @Override
    public void run() {
        for (int i = 0; i < orderNumer ; i ++) {
            System.out.println("orderNumer = " + orderNumer + " : " + Thread.currentThread().getName());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

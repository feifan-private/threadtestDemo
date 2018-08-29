package com.thread.orderedthread;

public class ThreadoFirst implements Runnable{
    private int orderNumer;
    ThreadoFirst(int orderNumer){
        this.orderNumer = orderNumer;
    }
    @Override
    public void run() {
        for (int i = 0; i < orderNumer ; i ++) {
            //打印出线程被添加的序号和线程名称
            System.out.println("orderNumer = " + orderNumer + " : " + Thread.currentThread().getName());
            //模拟业务场景消耗
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

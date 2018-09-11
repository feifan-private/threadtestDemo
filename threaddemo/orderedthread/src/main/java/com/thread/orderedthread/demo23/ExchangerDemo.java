package com.thread.orderedthread.demo23;

import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceNodeSetData;

import java.util.concurrent.Exchanger;

class Car implements Runnable{
    private Exchanger<String> exchanger;
    public Car(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }
    public void run() {
        try {
            System.out.println("car " + Thread.currentThread().getName() + ":" + exchanger.exchange("Car"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Bike implements Runnable{
    private Exchanger<String> exchanger;
    public Bike(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }
    public void run() {
        try {
            System.out.println("bike " + Thread.currentThread().getName() + ":" + exchanger.exchange("Bike"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<String>();
        Thread car = new Thread(new Car(exchanger));
        Thread bike = new Thread(new Bike(exchanger));
        car.start();
        bike.start();
        System.out.println("Main end!");
    }
}

package com.thread.orderedthread.demo21;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GreenhouseScheduler {
    private volatile boolean light = false;
    private volatile boolean water = false;
    private String thermostat = "Day";

    public synchronized String getThermostat() {
        return thermostat;
    }

    public synchronized void setThermostat(String thermostat) {
        this.thermostat = thermostat;
    }
    ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(10);
    public void schedule(Runnable event, long delay){
        scheduler.schedule(event,delay, TimeUnit.MILLISECONDS);
    }
    public void repeat(Runnable event, long initialDelay, long period){
        scheduler.scheduleAtFixedRate(event,initialDelay,period, TimeUnit.MILLISECONDS);
    }
    class LightOn implements Runnable{
        public void run() {
            System.out.println("Turning on lights");
            light = true;
        }
    }
    class LightOff implements Runnable{
        public void run() {
            System.out.println("Turning off lights");
            light = false;
        }
    }
    class WaterOn implements Runnable{
        public void run() {
            System.out.println("Turning greenhosue water on");
            water = true;
        }
    }
    class waterOff implements Runnable{
        public void run() {
            System.out.println("Turing greenhouse water off");
            water = false;
        }
    }
    class ThermostatNight implements Runnable{
        public void run() {
            System.out.println("thermostat to night setting");
            setThermostat("Night");
        }
    }
    class ThermostatDay implements Runnable{
        public void run() {
            System.out.println("Thremostat to day setting");
            setThermostat("Day");
        }
    }
    class Bell implements Runnable{
        public void run() {
            System.out.println("Bing");
        }
    }
    class Terminate implements Runnable{
        public void run() {
            System.out.println("Terminating");
            scheduler.shutdownNow();
            new Thread(){
                public void run(){

                }
            }.start();

        }
    }
    static class DataPoint{

    }
}

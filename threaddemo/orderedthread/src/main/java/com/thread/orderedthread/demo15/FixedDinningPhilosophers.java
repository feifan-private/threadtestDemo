package com.thread.orderedthread.demo15;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedDinningPhilosophers {
    public static void main(String[] args) throws IOException {
        int ponder = 0;
        if(args.length > 0){
            ponder = Integer.parseInt(args[0]);
        }
        int sise = 5;
        if (args.length > 1){
            sise = Integer.parseInt(args[1]);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[sise];
        for (int i = 0; i < sise; i++){
            sticks[i] = new Chopstick();
        }
        for (int i = 0; i< sise; i++){
            if (i < sise - 1)
                exec.execute(new Philosopher(sticks[i],sticks[i+1],i,ponder));
            else
                exec.execute(new Philosopher(sticks[0],sticks[i],i,ponder));
        }
        if (args.length == 3 && args[2].equals("timeout")){
            System.out.println("press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdownNow();

    }
}

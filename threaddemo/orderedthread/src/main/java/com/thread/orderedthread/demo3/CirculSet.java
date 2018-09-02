package com.thread.orderedthread.demo3;

public class CirculSet {
    private int[] array;
    private int len;
    private int index = 0;
    public CirculSet(int size){
        array = new int[size];
        len = size;
        for (int i = 0; i < len; i++){
            array[i] = -1;
        }
    }
    public synchronized void add(int i){
        array[index] = i;
        index = ++index%len;
    }
    public synchronized  boolean contains(int val){
        for (int i = 0; i < len; i++){
            if(array[i] == val)
                return true;
        }
        return false;
    }
}
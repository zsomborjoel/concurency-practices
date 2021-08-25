package com.concurency.practices;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultipleLocks {

    private Random random = new Random();

    // good practice to declare separate lock objects
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    // shared data on multiple threads
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();

    public void stageOne() {

        // with synchronized code block two threads can run it same time
        synchronized (lock1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            list1.add(random.nextInt(100));
        }

    }

    public void stageTwo() {

        synchronized (lock2) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            list2.add(random.nextInt(100));
        }

    }

    public void process() {
        for (int i = 0; i < 1000; i++) {
            stageOne();
            stageTwo();
        }
    }

    public void run() {
        System.out.println("Starting ...");
        long start = System.currentTimeMillis();

        Thread thread1 = new Thread(this::process);
        Thread thread2 = new Thread(this::process);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Time it took: " + (end - start) + " ms");
        System.out.println("Size list1: " + list1.size() + " , size list2: " + list2.size());
    }

    public static void main(String[] args) {
        MultipleLocks multipleLocks = new MultipleLocks();
        multipleLocks.run();
    }
}
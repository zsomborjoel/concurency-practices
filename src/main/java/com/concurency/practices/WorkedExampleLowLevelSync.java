package com.concurency.practices;

import java.util.LinkedList;
import java.util.Random;

class Processor3 {

    private LinkedList<Integer> list = new LinkedList<>();
    private final int LIMIT = 10;
    private Object lock = new Object();

    public void produce() throws InterruptedException {
        int value = 0;

        while (true) {

            synchronized (lock) {

                // Checking that the condition not applies any more why we use wait()
                while (list.size() == LIMIT) {
                    // lock on object which synchronized on
                    lock.wait();
                }
                list.add(value++);
                lock.notify(); // wakes up consume
            }
        }
    }

    public void consume() throws InterruptedException {

        Random random = new Random();

        while (true) {

            synchronized (lock) {

                while (list.size() == 0) {
                    lock.wait();
                }
                System.out.print("List size is: " + list.size());
                int value = list.removeFirst();
                System.out.println("; value is: " + value);
                lock.notify(); // wakes up produce
            }
            Thread.sleep(random.nextInt(1000));
        }
    }
}

class WorkedExampleLowLevelSync {

    public static void main(String[] args) {
        Processor3 processor3 = new Processor3();

        Thread t1 = new Thread(() -> {
            try {
                processor3.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                processor3.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

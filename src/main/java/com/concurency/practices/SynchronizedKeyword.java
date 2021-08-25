package com.concurency.practices;

public class SynchronizedKeyword {

    // easiest way to fix multiple thread using same variable by using AtomicInteger class
    private int count = 0;

    // other thread need to wait for Intrinsic Lock by synchronized
    public synchronized void increment() {
        count++;
    }

    public static void main(String[] args) {
        SynchronizedKeyword synchronizedKeyword = new SynchronizedKeyword();
        synchronizedKeyword.work();
    }

    public void work() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i ++) {
                increment();
                // count++ is 3 steps
                // count = count + 1;
                // takes time
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i ++) {
                increment();
            }
        });

        thread1.start();
        thread2.start();

        // won't return until thread finished
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }

        System.out.println("Count is: " + count);
    }

}

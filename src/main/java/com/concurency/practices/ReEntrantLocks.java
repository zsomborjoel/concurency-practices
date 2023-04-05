package com.concurency.practices;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Runner {

    private int count = 0;
    private Lock lock = new ReentrantLock(); // Thread can lock it multiple times
    private Condition condition = lock.newCondition();

    private void increment() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public void firstThread() throws InterruptedException {
        lock.lock(); // aquires lock

        System.out.println("Waiting...");
        condition.await(); // hands over lock to secound
        System.out.println("Woken up!");

        // have to unlock in finally as it will never call if method throws exception
        try {
            increment();
        } finally {
            lock.unlock(); // releases lock
        }
    }

    public void secondThread() throws InterruptedException {
        Thread.sleep(1000);
        lock.lock(); // aquires lock

        System.out.println("Press the return key");
        new Scanner(System.in).nextLine();
        System.out.println("Got return key");

        condition.signal(); // signals but have to call unlock also

        // have to unlock in finally as it will never call if method throws exception
        try {
            increment();
        } finally {
            lock.unlock(); // releases lock
        }
    }

    public void finished() throws InterruptedException {
        System.out.println("Count is: " + count);
    }
}

// Alternative to synchronized keyword
public class ReEntrantLocks {

    public static void main(String[] args) throws InterruptedException {

        final Runner runner = new Runner();

        Thread t1 = new Thread(() -> {
            try {
                runner.firstThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                runner.secondThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        runner.finished();
    }

}

package com.concurency.practices;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
    private int balance = 10000;

    public void deposit(int amount) {
        this.balance += amount;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public int getBalance() {
        return balance;
    }

    public static void transfer(Account acc1, Account acc2, int amount) {
        acc1.withdraw(amount);
        acc2.deposit(amount);
    }
}

class Runner3 {

    private Account acc1 = new Account();
    private Account acc2 = new Account();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    private void acquireLocks(Lock firstLock, Lock secondLock) throws InterruptedException {
        while (true) {
            // Aquire locks
            boolean gotFirstLock = false;
            boolean gotSecondLock = false;

            try {
                gotFirstLock = firstLock.tryLock();
                gotSecondLock = secondLock.tryLock();
            } finally {
                if (gotFirstLock && gotSecondLock) {
                    return;
                }

                if (gotFirstLock) {
                    firstLock.unlock();
                }

                if (gotSecondLock) {
                    secondLock.unlock();
                }
            }

            // Locks not acquired
            Thread.sleep(1);
        }
    }

    public void firstThread() throws InterruptedException {
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {

            acquireLocks(lock1, lock2);
            /*
            lock1.lock();
            lock2.lock(); // DEADLOCK --> second thread acquired LOCK2 before
            */
            try {
                Account.transfer(acc1, acc2, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }

        }
    }

    public void secondThread() throws InterruptedException {
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {

            acquireLocks(lock2, lock1);
            /*
            lock2.lock();
            lock1.lock(); // DEADLOCK --> first thread acquired LOCK1 before
            */
            try {
                Account.transfer(acc2, acc1, random.nextInt(100));
            } finally {
                lock2.unlock();
                lock1.unlock();
            }

        }
    }

    public void finished() throws InterruptedException {
        System.out.println("Acc1 balance: " + acc1.getBalance());
        System.out.println("Acc2 balance: " + acc2.getBalance());
        System.out.println("Total: " + (acc1.getBalance() + acc2.getBalance()));
    }
}

/**
 * Deadlock can occur if lock lock in different order on different threads
 *
 * Resolve:
 * 1) lock locks in same order
 *
 */
public class DeadLock {

    public static void main(String[] args) throws InterruptedException {

        final Runner3 runner3 = new Runner3();

        Thread t1 = new Thread(() -> {
            try {
                runner3.firstThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                runner3.secondThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        runner3.finished();
    }

}


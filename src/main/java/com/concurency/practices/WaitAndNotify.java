package com.concurency.practices;

import java.util.Scanner;

class Processor2 {
    public void produce() throws InterruptedException {

        // 1)
        synchronized (this) {
            System.out.println("Producer thread running....");

            // 2)
            // method of Object class - more resource efficient than loops
            // removes lock
            wait(50000);
            // 5) after notify
            System.out.println("Resumed");
        }
    }

    public void consume() throws InterruptedException {
        // 3)
        Scanner scanner = new Scanner(System.in);
        // runs first
        Thread.sleep(2000);

        // 4)
        synchronized (this) {
            System.out.println("Waiting for return key");
            scanner.nextLine();
            System.out.println("Return key pressed");
            // only can be called synchronized code block - notify one other thread which locker on this object
            // does not remove lock
            notify();
        }
    }
}

public class WaitAndNotify {

    public static void main(String[] args) {
        Processor2 processor2 = new Processor2();

        Thread t1 = new Thread(() -> {
            try {
                processor2.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                processor2.consume();
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

package com.concurency.practices;

import java.util.Scanner;

class Processor extends Thread {

    // volatile prevents caching variables
    private volatile boolean running = true;

    // not expected other thread will modify running this thread
    public void run() {

        int i = 0;
        while(running) {
            System.out.println("Hello" + (i += 1));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}

public class  ThreadSynchronization {

    /**
     * Two threading running here. Main is one thread and other thread is
     * Porcessor thread after started.
     *
     * @param args
     */
    public static void main(String[] args) {
        Processor processor = new Processor();
        processor.start();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // gracefully shutdown by main thread
        processor.shutdown();
    }

}

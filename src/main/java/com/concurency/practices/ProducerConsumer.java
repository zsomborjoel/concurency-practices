package com.concurency.practices;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {

    // Thread safe (everything in concurrent package)
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    private static void producer() throws InterruptedException {
        Random random = new Random();

        // testing purposes
        while (true) {
            queue.put(random.nextInt(100));
        }
    }

    private static void consumer() throws InterruptedException {
        Random random = new Random();

        // testing purposes
        while (true) {
            Thread.sleep(100);

            // once per second
            if (random.nextInt(10) == 0) {
                // waits until something added in the queue
                // no resource used while not used - use same system resources
                Integer value = queue.take();

                System.out.println("Taken value: " + value + "; Queue size is: " + queue.size());
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                producer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                consumer();
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

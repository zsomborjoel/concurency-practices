package com.concurency.practices;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ProcessorTp implements Runnable {

    private int id;

    public ProcessorTp(int id) {
        this.id = id;
    }

    public void run() {
        System.out.println("Starting: " + id);

        // handling requests or processing files example
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed: " + id);
    }

}

public class ThreadPools {

    public static void main(String[] args) {

        // want these threads to do tasks - start new one if one finished
        // thread pool recycles threads and removes overhead from creating new Threads;
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Five task
        for (int i = 0; i < 5; i++) {
            executorService.submit(new ProcessorTp(i));
        }

        // wait for threads finish what they are doing
        executorService.shutdown();

        System.out.println("All tasks submitted.");

        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");
    }
}
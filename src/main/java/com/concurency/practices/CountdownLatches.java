package com.concurency.practices;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ProcessorCl implements Runnable {
    private CountDownLatch latch;

    ProcessorCl (CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Started ...");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // counted by down one
        latch.countDown();
    }

}

public class CountdownLatches {

    public static void main(String[] args) {

        // count down on threads (Thread safe)
        CountDownLatch latch = new CountDownLatch(3);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3 ; i++) {
            executorService.submit(new ProcessorCl(latch));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed.");
    }
}

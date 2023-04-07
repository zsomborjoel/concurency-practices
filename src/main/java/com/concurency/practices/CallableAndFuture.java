package com.concurency.practices;

import java.util.Random;
import java.util.concurrent.*;

// Allow thread code to throw exception
public class CallableAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Callable
        Future<Integer> future = executor.submit(() -> {
            Random random = new Random();
            int duration = random.nextInt(4000);

            if (duration > 2000) {
                throw new RuntimeException("Sleeping for too long");
            }

            System.out.println("Starting...");

            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Finished...");

            return duration;
        });

        executor.shutdown();

        try {
            System.out.println("Result: " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println(e);
        }
    }
}

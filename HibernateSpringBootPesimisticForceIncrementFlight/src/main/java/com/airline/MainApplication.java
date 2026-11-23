package com.airline;

import com.airline.service.SeatBookingService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootApplication
public class MainApplication {

    private final SeatBookingService seatBookingService;

    public MainApplication(SeatBookingService seatBookingService) {
        this.seatBookingService = seatBookingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
                       
            // USe flight 1001 (starts at version = 0)            

            CountDownLatch loadedLatch = new CountDownLatch(1);
            CountDownLatch proceedToCommitLatch = new CountDownLatch(1);

            try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

                // thread A, optimistic capacity update (loads version = 0, then pauses)
                Future<?> gateAgentTask = executor.submit(() -> {
                    try {
                        seatBookingService.updateAircraftCapacityAuto(
                                1001L, 1, loadedLatch, proceedToCommitLatch);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                // wait until thread A has loaded Flight (version = 0)
                loadedLatch.await();

                // thread b, PESSIMISTIC_FORCE_INCREMENT seat booking (modify version 0 to 1 and commits)
                Future<?> passengerTask = executor.submit(() -> {
                    seatBookingService.assignSeat(50001L, 1001L, "1A", "Elena Popescu");
                    System.out.println("[Thread B - Booking] Seat assigned! Flight version updated to 1.");
                });
                passengerTask.get(); // ensure thread B commits first

                // release thread A so it tries to commit against stale version = 0
                proceedToCommitLatch.countDown();

                try {
                    gateAgentTask.get();
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof ObjectOptimisticLockingFailureException ex) {
                        System.out.println("[SUCCESS] Hibernate automatically threw: " + ex.getClass().getSimpleName());
                    } else {
                        throw e;
                    }
                }
            }
        };
    }
}

package com.airline.service;

import com.airline.entity.Flight;
import com.airline.entity.SeatAssignment;
import org.springframework.stereotype.Service;
import com.airline.repository.FlightRepository;
import com.airline.repository.SeatAssignmentRepository;
import java.util.concurrent.CountDownLatch;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatBookingService {

    private final FlightRepository flightRepository;
    private final SeatAssignmentRepository seatRepository;

    public SeatBookingService(FlightRepository flightRepository,
            SeatAssignmentRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public SeatAssignment assignSeat(Long assignmentId, Long flightId, String seatCode, String passengerName) {
        
        // acquires lock and immediately executes: 
        // UPDATE flights SET version = version + 1 WHERE id = ? AND version = ?
        Flight flight = flightRepository.findByIdWithForceIncrement(flightId).orElseThrow();

        if (seatRepository.countByFlightId(flightId) >= flight.getCapacity()) {
            throw new IllegalStateException("Flight is full");
        }

        SeatAssignment sa = new SeatAssignment();
        sa.setId(assignmentId);
        sa.setFlight(flight);
        sa.setSeatCode(seatCode);
        sa.setPassengerName(passengerName);
        
        return seatRepository.save(sa);
    }

    @Transactional
    public void updateAircraftCapacityAuto(Long flightId,
                                           int newCapacity,
                                           CountDownLatch loadedLatch,
                                           CountDownLatch proceedToCommitLatch) throws InterruptedException {
        // loaded when version = 0
        Flight flight = flightRepository.findById(flightId).orElseThrow();
        System.out.println("[Thread A - Gate Agent] Loaded Flight version = " + flight.getVersion());

        // signal Thread B that Thread A now holds a stale snapshot (version = 0)
        loadedLatch.countDown();

        // wait until Thread B finishes PESSIMISTIC_FORCE_INCREMENT and commits (version -> 1)
        proceedToCommitLatch.await();

        // mutate entity; Hibernate will automatically check WHERE id = 1001 AND version = 0 at commit!
        flight.setCapacity(newCapacity);
        System.out.println("[Thread A - Gate Agent] Attempting to commit capacity change...");
    }
}

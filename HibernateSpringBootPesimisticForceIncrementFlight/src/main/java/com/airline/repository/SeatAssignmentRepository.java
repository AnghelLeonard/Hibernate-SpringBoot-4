package com.airline.repository;

import com.airline.entity.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface SeatAssignmentRepository extends JpaRepository<SeatAssignment, Long> {
    
    long countByFlightId(Long flightId);
    boolean existsByFlightIdAndSeatCode(Long flightId, String seatCode);
}

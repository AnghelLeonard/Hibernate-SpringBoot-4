package com.airline.repository;

import com.airline.entity.Flight;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @QueryHints({
        @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")
    })
    @Query("SELECT f FROM Flight f WHERE f.id = :id")
    Optional<Flight> findByIdWithForceIncrement(@Param("id") Long id);
}

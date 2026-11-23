package com.billing.repository;

import com.billing.entity.TaxRate;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TaxRateRepository extends JpaRepository<TaxRate, String> {

    // Acquires a shared lock (e.g., SELECT ... FOR SHARE in MySQL/PostgreSQL).
    // Multiple transactions can hold this simultaneously,
    // but any UPDATE to this row is blocked until active readers commit.
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT t FROM TaxRate t WHERE t.countryCode = :countryCode")
    Optional<TaxRate> findWithSharedLock(@Param("countryCode") String countryCode);
}

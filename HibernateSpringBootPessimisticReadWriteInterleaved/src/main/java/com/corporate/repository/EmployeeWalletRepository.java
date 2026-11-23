package com.corporate.repository;

import com.corporate.entity.EmployeeWallet;
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
public interface EmployeeWalletRepository extends JpaRepository<EmployeeWallet, String> {

    // exclusive lock, serializes balance changes for this specific employee
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM EmployeeWallet w WHERE w.employeeId = :employeeId")
    Optional<EmployeeWallet> findByIdForUpdate(@Param("employeeId") String employeeId);
}

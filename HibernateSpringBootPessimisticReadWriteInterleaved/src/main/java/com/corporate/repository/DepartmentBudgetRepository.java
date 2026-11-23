package com.corporate.repository;

import com.corporate.entity.DepartmentBudget;
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
public interface DepartmentBudgetRepository extends JpaRepository<DepartmentBudget, String> {

    // shared lock, multiple employee transfers can verify the limit simultaneously,
    // while preventing a manager from lowering the limit mid-transfer
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT d FROM DepartmentBudget d WHERE d.deptCode = :deptCode")
    Optional<DepartmentBudget> findWithSharedLock(@Param("deptCode") String deptCode);

    // exclusive lock for the finance manager updating the policy
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM DepartmentBudget d WHERE d.deptCode = :deptCode")
    Optional<DepartmentBudget> findForUpdate(@Param("deptCode") String deptCode);
}

package com.corporate.service;

import com.corporate.entity.DepartmentBudget;
import com.corporate.entity.EmployeeWallet;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import com.corporate.repository.DepartmentBudgetRepository;
import com.corporate.repository.EmployeeWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);

    private final DepartmentBudgetRepository budgetRepository;
    private final EmployeeWalletRepository walletRepository;

    public ExpenseService(DepartmentBudgetRepository budgetRepository,
            EmployeeWalletRepository walletRepository) {
        this.budgetRepository = budgetRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void topUpWallet(String employeeId, BigDecimal amount) {
        String thread = Thread.currentThread().getName();

        // Step 1 - acquire PESSIMISTIC_READ on the shared department policy
        // notice that concurrent transfers pass through here simultaneously
        DepartmentBudget budget = budgetRepository.findWithSharedLock("ENG").orElseThrow();
        
        log.info("[{}] Acquired PESSIMISTIC_READ on ENG limit ({})", thread, budget.getMaxTransferLimit());

        if (amount.compareTo(budget.getMaxTransferLimit()) > 0) {
            throw new IllegalStateException("Amount exceeds department transfer limit!");
        }

        // Step 2 - acquire PESSIMISTIC_WRITE on the specific employee's wallet
        // if Alice double-clicked, the 2nd thread waits right here until the 1st commits
        EmployeeWallet wallet = walletRepository.findByIdForUpdate(employeeId).orElseThrow();
        
        log.info("[{}] Acquired PESSIMISTIC_WRITE on {}'s wallet (current balance: {})",
                thread, employeeId, wallet.getBalance());

        // simulate processing delay so interleaving is clearly visible in logs
        sleep(1500);

        wallet.setBalance(wallet.getBalance().add(amount));
        log.info("[{}] Updated {}'s balance to {}", thread, employeeId, wallet.getBalance());
    }

    @Transactional
    public void updateDepartmentLimit(String deptCode, BigDecimal newLimit) {
        
        String thread = Thread.currentThread().getName();
        log.info("[{}] Manager requesting PESSIMISTIC_WRITE to change {} limit to {}...",
                thread, deptCode, newLimit);

        // waits until ALL active PESSIMISTIC_READ locks on 'ENG' are released
        DepartmentBudget budget = budgetRepository.findForUpdate(deptCode).orElseThrow();

        budget.setMaxTransferLimit(newLimit);
        log.info("[{}] Manager successfully updated {} limit to {}", thread, deptCode, newLimit);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

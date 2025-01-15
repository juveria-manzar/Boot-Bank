package com.juveriatech.demo.repository;

import com.juveriatech.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(Long accountNumber);
}

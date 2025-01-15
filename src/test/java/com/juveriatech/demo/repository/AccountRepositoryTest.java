package com.juveriatech.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    AccountRepository accountRepository;



    @Test
    void testExistsByAccountNumber() {
        Customer customer = new Customer(1L, "Sam", null);
        testEntityManager.persistAndFlush(customer);

        Account account = new Account();
        account.setAccountNumber(123456L);
        account.setBalance(3000.0);
        account.setCustomer(customer);
        testEntityManager.persistAndFlush(account);
        assertTrue(accountRepository.existsByAccountNumber(123456L));
    }
}
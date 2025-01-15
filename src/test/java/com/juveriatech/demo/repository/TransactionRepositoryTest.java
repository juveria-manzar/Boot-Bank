package com.juveriatech.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Customer;
import com.juveriatech.demo.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TransactionRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void testFindByAccountId() {
        Customer customer = new Customer();
        customer.setName("Sam");
        testEntityManager.persistAndFlush(customer);

        Account account = new Account();
        account.setAccountNumber(123456L);
        account.setBalance(3000.0);
        account.setCustomer(customer);
        testEntityManager.persistAndFlush(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(100.0);
        transaction.setTransactionDate(LocalDateTime.now());
        testEntityManager.persistAndFlush(transaction);

        Page<Transaction> transactions = transactionRepository.findByAccountId(account.getId(), PageRequest.of(0, 10));

        assertThat(transactions).isNotEmpty();
        assertThat(transactions.getContent().get(0).getAmount()).isEqualTo(100.0);
    }
}
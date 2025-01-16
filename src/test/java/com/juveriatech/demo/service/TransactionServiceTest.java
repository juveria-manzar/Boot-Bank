package com.juveriatech.demo.service;

import com.juveriatech.demo.dto.TransactionDto;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Transaction;
import com.juveriatech.demo.repository.AccountRepository;
import com.juveriatech.demo.repository.TransactionRepository;
import com.juveriatech.demo.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Transaction can be created")
    public void testCreateTransaction() {
        Long accountId = 1L;
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(100.0);
        transactionDto.setMode("DEBIT");

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(3000.0);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setMode("DEBIT");
        transaction.setAccount(account);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDto createdTransaction = transactionService.makeTransaction(accountId, transactionDto);

        assertEquals(transactionDto.getAmount(), createdTransaction.getAmount());
        assertEquals(transactionDto.getMode(), createdTransaction.getMode());
    }

}
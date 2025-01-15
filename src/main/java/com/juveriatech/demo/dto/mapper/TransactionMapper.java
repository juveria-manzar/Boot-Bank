package com.juveriatech.demo.dto.mapper;

import com.juveriatech.demo.dto.TransactionDto;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Transaction;

public class TransactionMapper {
    public static TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(), transaction.getAmount(), transaction.getMode(), transaction.getTransactionDate(), transaction.getAccount().getId());
    }

    public static Transaction toEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setMode(transactionDto.getMode());
        transaction.setAmount(transactionDto.getAmount());

        Account account = new Account();
        account.setId(transactionDto.getAccountId());
        transaction.setAccount(account);

        return transaction;
    }
}
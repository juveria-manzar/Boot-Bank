package com.juveriatech.demo.service.impl;

import com.juveriatech.demo.dto.TransactionDto;
import com.juveriatech.demo.dto.mapper.TransactionMapper;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Transaction;
import com.juveriatech.demo.exception.BadRequestException;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.repository.AccountRepository;
import com.juveriatech.demo.repository.TransactionRepository;
import com.juveriatech.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public TransactionDto makeTransaction(Long accountId, TransactionDto transactionDto) {

        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("Account doesn't exist"));

        Transaction transaction = new Transaction();
        transaction.setMode(transactionDto.getMode());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAmount(transactionDto.getAmount());

        if(transaction.getAmount() < 0){
            throw new BadRequestException("Invalid amount");
        }
        if(transaction.getMode().equals("DEBIT")){
            if(account.getBalance() < transaction.getAmount()){
                throw new BadRequestException("Insufficient balance");
            }else{
                account.setBalance(account.getBalance() - transaction.getAmount());
            }
        } else {
            account.setBalance(account.getBalance() + transaction.getAmount());
        }

        transaction.setAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toDto(savedTransaction);
    }

    @Override
    public Page<TransactionDto> getCustomerTransactions(Long accountId, int offset, int pageSize) {
        return transactionRepository.findByAccountId(accountId, PageRequest.of(offset, pageSize)).map(TransactionMapper::toDto);
    }
}
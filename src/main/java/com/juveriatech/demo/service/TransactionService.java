package com.juveriatech.demo.service;

import com.juveriatech.demo.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Page<TransactionDto> getCustomerTransactions(Long accountId, int offset, int pageSize);
    TransactionDto makeTransaction(Long accountId, TransactionDto transactionDto);
}
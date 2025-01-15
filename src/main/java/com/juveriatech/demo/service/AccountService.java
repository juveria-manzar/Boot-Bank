package com.juveriatech.demo.service;

import com.juveriatech.demo.dto.AccountDto;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto, Long customerId);
    AccountDto getAccountById(Long id);
    void deleteAccountById(Long id);
}

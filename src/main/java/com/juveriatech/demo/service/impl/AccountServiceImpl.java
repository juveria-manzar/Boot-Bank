package com.juveriatech.demo.service.impl;

import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.dto.mapper.AccountMapper;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Customer;
import com.juveriatech.demo.exception.BadRequestException;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.repository.AccountRepository;
import com.juveriatech.demo.repository.CustomerRepository;
import com.juveriatech.demo.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    @Override
    public AccountDto createAccount(AccountDto accountDto, Long customerId) {
        if (accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
            throw new BadRequestException("Account number already exists");
        }
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + accountDto.getCustomerId() + " is not found"));
        if (customer.getAccount() != null) {
            throw new BadRequestException("Customer already has an account linked");
        }
        Account account = new Account();
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
        account.setCustomer(customer);
        customer.setAccount(account);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.toDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account with id " + id + " not found"));
        return AccountMapper.toDto(account);
    }

    @Transactional
    @Override
    public void deleteAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + id + " not found"));
        accountRepository.delete(account);
    }

}
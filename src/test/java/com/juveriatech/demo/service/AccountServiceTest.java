package com.juveriatech.demo.service;

import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Customer;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.repository.AccountRepository;
import com.juveriatech.demo.repository.CustomerRepository;
import com.juveriatech.demo.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    @DisplayName("Account can be created")
    public void testCreateAccount() {
        Long customerId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(123456L);
        accountDto.setBalance(3000.0);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Sam");

        Account account = new Account();
        account.setAccountNumber(123456L);
        account.setBalance(3000.0);
        account.setCustomer(customer);

        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDto createdAccount = accountService.createAccount(accountDto, customerId);

        assertEquals(accountDto.getAccountNumber(), createdAccount.getAccountNumber());
        assertEquals(accountDto.getBalance(), createdAccount.getBalance());
    }

    @Test
    @DisplayName("Get account by ID")
    public void testGetAccountById() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber(123456L);
        account.setBalance(3000.0);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountDto accountDto = accountService.getAccountById(accountId);

        assertEquals(account.getAccountNumber(), accountDto.getAccountNumber());
        assertEquals(account.getBalance(), accountDto.getBalance());
    }

    @Test
    @DisplayName("Get account by ID - not found")
    public void testGetAccountByIdNotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(accountId));
    }

    @Test
    @DisplayName("Delete account by ID")
    public void testDeleteAccountById() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccountById(accountId);

        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    @DisplayName("Delete account by ID - not found")
    public void testDeleteAccountByIdNotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccountById(accountId));
    }
}

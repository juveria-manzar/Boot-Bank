package com.juveriatech.demo.dto.mapper;

import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Customer;

public class AccountMapper {

    public static AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }

        if(account.getCustomer()==null){
            return new AccountDto(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getBalance(),
                    null
            );
        }

        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCustomer().getId()
        );
    }

    public static Account toEntity(AccountDto accountDto) {
        if (accountDto == null) {
            return null;
        }
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());

        if(accountDto.getCustomerId()!=null){
            Customer customer = new Customer();
            customer.setId(accountDto.getCustomerId());
            account.setCustomer(customer);
        }
        return account;
    }
}
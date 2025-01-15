package com.juveriatech.demo.dto.mapper;

import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.dto.CustomerDto;
import com.juveriatech.demo.entity.Account;
import com.juveriatech.demo.entity.Customer;

public class CustomerMapper {
    public static CustomerDto toDto(Customer customer) {
        AccountDto accountDto = customer.getAccount() != null ? AccountMapper.toDto(customer.getAccount()) : null;
        return new CustomerDto(customer.getId(), customer.getName(), accountDto);
    }

    public static Customer toEntity(CustomerDto customerDto) {
        Customer customer = new Customer(customerDto.getId(), customerDto.getName());
        if (customerDto.getAccount() != null) {
            Account account = AccountMapper.toEntity(customerDto.getAccount());
            customer.setAccount(account);
        }
        return customer;
    }
}
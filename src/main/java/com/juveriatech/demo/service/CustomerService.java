package com.juveriatech.demo.service;

import com.juveriatech.demo.dto.CustomerDto;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customer);
    CustomerDto getCustomerById(Long id);
}

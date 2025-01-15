package com.juveriatech.demo.service.impl;

import com.juveriatech.demo.dto.CustomerDto;
import com.juveriatech.demo.dto.mapper.CustomerMapper;
import com.juveriatech.demo.entity.Customer;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.repository.CustomerRepository;
import com.juveriatech.demo.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toDto(savedCustomer);
    }

    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return CustomerMapper.toDto(customer);
    }
}
package com.juveriatech.demo.service;

import com.juveriatech.demo.dto.CustomerDto;
import com.juveriatech.demo.entity.Customer;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.repository.CustomerRepository;
import com.juveriatech.demo.service.impl.CustomerServiceImpl;
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
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    @DisplayName("Customer can be created")
    public void testCreateCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Sam");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Sam");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto createdCustomer = customerService.createCustomer(customerDto);

        assertEquals(customerDto.getName(), createdCustomer.getName());
    }

    @Test
    @DisplayName("Get customer by ID")
    public void testGetCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Sam");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerDto customerDto = customerService.getCustomerById(customerId);

        assertEquals(customer.getId(), customerDto.getId());
        assertEquals(customer.getName(), customerDto.getName());
    }

    @Test
    @DisplayName("Get customer by ID - not found")
    public void testGetCustomerByIdNotFound() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

}
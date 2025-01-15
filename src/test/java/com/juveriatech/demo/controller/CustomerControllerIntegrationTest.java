package com.juveriatech.demo.controller;

import com.juveriatech.demo.dto.CustomerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private CustomerDto getCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setName("Sam");
        return customer;
    }

    @Test
    @DisplayName("Customer can be Created")
    public void testCreateCustomer() {
        CustomerDto customerDto = getCustomer();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CustomerDto> entity = new HttpEntity<>(customerDto, headers);

        ResponseEntity<CustomerDto> response = testRestTemplate.postForEntity(
                "/api/customers", entity, CustomerDto.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Customer creation failed");
        Assertions.assertNotNull(response.getBody(), "Response body is null");
        Assertions.assertEquals(customerDto.getName(), response.getBody().getName(), "Customer name mismatch");
    }

    @Test
    @DisplayName("Get customer by ID")
    public void testGetCustomerById() {
        CustomerDto customerDto = getCustomer();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CustomerDto> entity = new HttpEntity<>(customerDto, headers);

        ResponseEntity<CustomerDto> createResponse = testRestTemplate.postForEntity(
                "/api/customers", entity, CustomerDto.class
        );

        Long customerId = createResponse.getBody().getId();
        Assertions.assertNotNull(customerId, "Customer ID is null");

        ResponseEntity<CustomerDto> response = testRestTemplate.getForEntity(
                "/api/customers/" + customerId, CustomerDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get customer by ID");
        Assertions.assertNotNull(response.getBody(), "Response body is null");
        Assertions.assertEquals(customerDto.getName(), response.getBody().getName(), "Customer name mismatch");
    }

    @Test
    @DisplayName("Delete customer by ID")
    public void testDeleteCustomerById() {
        CustomerDto customerDto = getCustomer();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CustomerDto> entity = new HttpEntity<>(customerDto, headers);

        ResponseEntity<CustomerDto> createResponse = testRestTemplate.postForEntity(
                "/api/customers", entity, CustomerDto.class
        );

        Long customerId = createResponse.getBody().getId();
        Assertions.assertNotNull(customerId, "Customer ID is null");

        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/customers/" + customerId, HttpMethod.DELETE, null, Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Failed to delete customer by ID");
    }

    @Test
    @DisplayName("Create customer with invalid data")
    public void testCreateCustomerWithInvalidData() {
        CustomerDto customerDto = new CustomerDto(); // Missing required fields

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CustomerDto> entity = new HttpEntity<>(customerDto, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/customers", entity, String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected BAD_REQUEST status");
    }
}
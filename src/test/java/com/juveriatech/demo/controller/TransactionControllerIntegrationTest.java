package com.juveriatech.demo.controller;

import com.juveriatech.demo.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Long customerId;
    private Long accountId;

    @BeforeEach
    void setUp() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Sam");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CustomerDto> customerEntity = new HttpEntity<>(customerDto, headers);
        ResponseEntity<CustomerDto> customerResponse = testRestTemplate.postForEntity("/api/customers", customerEntity, CustomerDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, customerResponse.getStatusCode(), "Customer not created");
        customerId = customerResponse.getBody().getId();
        Assertions.assertNotNull(customerId, "Customer ID is null");

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(123456L);
        accountDto.setBalance(3000.0);
        accountDto.setCustomerId(customerId);

        HttpEntity<AccountDto> accountEntity = new HttpEntity<>(accountDto, headers);
        ResponseEntity<AccountDto> accountResponse = testRestTemplate.postForEntity("/api/customers/" + customerId + "/accounts", accountEntity, AccountDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, accountResponse.getStatusCode(), "Account not created");
        accountId = accountResponse.getBody().getId();
        Assertions.assertNotNull(accountId, "Account ID is null");
    }

    private TransactionDto getTransaction() {
        TransactionDto transaction = new TransactionDto();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setMode("DEBIT");
        transaction.setAccountId(accountId);
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    @Test
    @DisplayName("Transaction can be Created")
    public void testCreateTransaction() {
        TransactionDto transactionDto = getTransaction();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<TransactionDto> response = testRestTemplate.postForEntity(
                "/api/accounts/" + accountId + "/transactions", entity, TransactionDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Transaction creation failed");
        Assertions.assertNotNull(response.getBody(), "Response body is null");
        Assertions.assertEquals(transactionDto.getAmount(), response.getBody().getAmount(), "Transaction amount mismatch");
    }

    @Test
    @DisplayName("Get transactions by Account ID")
    public void testGetTransactionsByAccountId() {
        TransactionDto transactionDto = getTransaction();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<TransactionDto> createResponse = testRestTemplate.postForEntity(
                "/api/accounts/" + accountId + "/transactions", entity, TransactionDto.class
        );

        Long transactionId = createResponse.getBody().getId();
        Assertions.assertNotNull(transactionId, "Transaction ID is null");
        ResponseEntity<PageResponse<TransactionDto>> response = testRestTemplate.exchange(
                "/api/accounts/" + accountId + "/transactions?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<TransactionDto>>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get transactions by account ID");
        Assertions.assertNotNull(response.getBody(), "Response body is null");
    }

    @Test
    @DisplayName("Create transaction with invalid data")
    public void testCreateTransactionWithInvalidData() {
        TransactionDto transactionDto = new TransactionDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/accounts/" + accountId + "/transactions", entity, String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected BAD_REQUEST status");
    }
}
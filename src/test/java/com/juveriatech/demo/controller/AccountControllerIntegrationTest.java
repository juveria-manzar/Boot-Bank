package com.juveriatech.demo.controller;

import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.dto.CustomerDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String URL = "/api/customers/{customerId}/accounts";
    private final String URL_WITH_ID = "/api/accounts/{accountId}";
    private Long customerId;

    @BeforeEach
    void setUp() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Sam");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<CustomerDto> entity = new HttpEntity<>(customerDto, headers);

        ResponseEntity<CustomerDto> customerResponse = testRestTemplate.postForEntity("/api/customers", entity, CustomerDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, customerResponse.getStatusCode(), "Customer not created");
        customerId = customerResponse.getBody().getId();
    }

    private AccountDto getAccount() {
        AccountDto account = new AccountDto();
        account.setId(1L);
        account.setAccountNumber(123456L);
        account.setBalance(3000.0);
        account.setCustomerId(customerId);
        return account;
    }



    @Test
    @DisplayName("Account can be Created")
    public void testCreateAccount() {
        AccountDto accountDto = getAccount();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<AccountDto> response = testRestTemplate.postForEntity(
                "/api/customers/" + customerId + "/accounts", entity, AccountDto.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Account creation failed");
        Assertions.assertNotNull(response.getBody(), "Response body is null");
        Assertions.assertEquals(accountDto.getBalance(), response.getBody().getBalance(), "Account balance mismatch");
    }

    @Test
    @DisplayName("Get account by ID")
    public void testGetAccountById() {
        AccountDto accountDto = getAccount();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<AccountDto> createResponse = testRestTemplate.postForEntity(
                "/api/customers/" + customerId + "/accounts", entity, AccountDto.class
        );

        Long accountId = createResponse.getBody().getId();

        ResponseEntity<AccountDto> response = testRestTemplate.getForEntity(
                "/api/accounts/"+accountId, AccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get account by ID");
        Assertions.assertNotNull(response.getBody(), "Response body is null");
        Assertions.assertEquals(accountDto.getBalance(), response.getBody().getBalance(), "Account balance mismatch");
    }

    @Test
    @DisplayName("Create account with invalid data")
    public void testCreateAccountWithInvalidData() {
        AccountDto accountDto = new AccountDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/customers/" + customerId + "/accounts", entity, String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected BAD_REQUEST status");
    }


    @Nested
    @DisplayName("Account and Customer required for Getting and deleting account")
    class SpecificTests {
        Long accountId;
        @BeforeEach
        void specificSetUp() {

            AccountDto accountDto = getAccount();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

            ResponseEntity<AccountDto> createResponse = testRestTemplate.postForEntity(
                    "/api/customers/" + customerId + "/accounts", entity, AccountDto.class
            );

            Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode(), "Account created");
            accountId = createResponse.getBody().getId();
        }
    }
}

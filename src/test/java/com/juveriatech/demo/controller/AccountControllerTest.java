package com.juveriatech.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.juveriatech.demo.controller.AccountController;
import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest(value = AccountController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountDto getAccount() {
        AccountDto account = new AccountDto();
        account.setId(1L);
        account.setBalance(3000);
        account.setCustomerId(1L);
        account.setAccountNumber(1234567890123456L);
        return account;
    }

    @Test
    @DisplayName("Account can be created")
    public void testCreateAccount_whenValidDetailsProvided_returnsCreatedAccount() throws Exception {
        AccountDto account = new AccountDto();
        account.setId(1L);
        account.setBalance(3000);
        account.setCustomerId(1L);
        account.setAccountNumber(1234567890123456L);

        AccountDto getAcct = getAccount();
        when(accountService.createAccount(any(AccountDto.class), any(Long.class))).thenReturn(getAcct);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customers/1/accounts")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(asJson(account));

        MvcResult mvcResult =  mvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        AccountDto createdAccount = new ObjectMapper().readValue(responseBodyAsString, AccountDto.class);
        Assertions.assertEquals(account.getBalance(), createdAccount.getBalance(), "Created Account balance is incorrect");
        Assertions.assertEquals(account.getAccountNumber(), createdAccount.getAccountNumber(), "Created Account number is incorrect");
        Assertions.assertFalse(createdAccount.getId() == null, "Created Account id should not be null");
    }

    @Test
    @DisplayName("Get account by ID")
    public void testGetAccountById_whenValidIdProvided_returnsAccount() throws Exception {
        AccountDto account = getAccount();
        when(accountService.getAccountById(1L)).thenReturn(account);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/1")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        AccountDto retrievedAccount = new ObjectMapper().readValue(responseBodyAsString, AccountDto.class);
        Assertions.assertEquals(account.getId(), retrievedAccount.getId(), "Retrieved Account ID is incorrect");
        Assertions.assertEquals(account.getBalance(), retrievedAccount.getBalance(), "Retrieved Account balance is incorrect");
        Assertions.assertEquals(account.getAccountNumber(), retrievedAccount.getAccountNumber(), "Retrieved Account number is incorrect");
    }

    @Test
    @DisplayName("Get account by ID - Invalid ID")
    public void testGetAccountById_whenInvalidIdProvided_returnsNotFound() throws Exception {
        when(accountService.getAccountById(999L)).thenThrow(new ResourceNotFoundException("Account not found"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/999")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        Assertions.assertEquals(404, mvcResult.getResponse().getStatus(), "Response status should be 404 Not Found");
    }

    @Test
    @DisplayName("Delete account by ID")
    public void testDeleteAccountById_whenValidIdProvided_returnsNoContent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/accounts/1")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();
        Assertions.assertEquals(204, mvcResult.getResponse().getStatus(), "Response status should be 204 No Content");
    }

    @Test
    @DisplayName("Delete account by ID - Invalid ID")
    public void testDeleteAccountById_whenInvalidIdProvided_returnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Account not found")).when(accountService).deleteAccountById(999L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/accounts/999")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        Assertions.assertEquals(404, mvcResult.getResponse().getStatus(), "Response status should be 404 Not Found");
    }

    @Test
    @DisplayName("Create account with invalid data")
    public void testCreateAccount_whenInvalidDetailsProvided_returnsBadRequest() throws Exception {
        AccountDto account = new AccountDto();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customers/1/accounts")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(asJson(account));

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
        Assertions.assertEquals(400, mvcResult.getResponse().getStatus(), "Response status should be 400 Bad Request");
    }

    private static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
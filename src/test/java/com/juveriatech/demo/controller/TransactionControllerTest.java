package com.juveriatech.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juveriatech.demo.dto.TransactionDto;
import com.juveriatech.demo.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest(value = TransactionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;

    private TransactionDto getTransaction() {
        TransactionDto transaction = new TransactionDto();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setMode("DEBIT");
        return transaction;
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("Make transaction with valid data")
    public void testMakeTransaction_whenValidDetailsProvided_returnsTransaction() throws Exception {
        TransactionDto transaction = getTransaction();
        when(transactionService.makeTransaction(any(Long.class), any(TransactionDto.class))).thenReturn(transaction);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/accounts/1/transactions")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(asJson(transaction));

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        TransactionDto createdTransaction = new ObjectMapper().readValue(responseBodyAsString, TransactionDto.class);
        Assertions.assertEquals(transaction.getAmount(), createdTransaction.getAmount(), "Transaction amount is incorrect");
        Assertions.assertFalse(createdTransaction.getId() == null, "Transaction ID should not be null");
    }

    @Test
    @DisplayName("Make transaction with invalid data")
    public void testMakeTransaction_whenInvalidDetailsProvided_returnsBadRequest() throws Exception {
        TransactionDto transaction = new TransactionDto();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/accounts/1/transactions")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(asJson(transaction));

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
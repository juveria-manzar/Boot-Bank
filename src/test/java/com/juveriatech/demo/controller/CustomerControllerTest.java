package com.juveriatech.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.juveriatech.demo.dto.CustomerDto;
import com.juveriatech.demo.exception.ResourceNotFoundException;
import com.juveriatech.demo.service.CustomerService;
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

@WebMvcTest(value = CustomerController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDto getCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        customer.setName("Sam");
        return customer;
    }

    @Test
    @DisplayName("Customer can be created")
    public void testCreateCustomer_whenValidDetailsProvided_returnsCreatedCustomer() throws Exception {
        CustomerDto customer = new CustomerDto();
        customer.setName("Sam");

        CustomerDto getCust = getCustomer();
        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(getCust);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customers")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(asJson(customer));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        CustomerDto createdCustomer = new ObjectMapper().readValue(responseBodyAsString, CustomerDto.class);
        Assertions.assertEquals(customer.getName(), createdCustomer.getName(), "Created Customer name is incorrect");
        Assertions.assertFalse(createdCustomer.getId() == null, "Created Customer id should not be null");
    }

    @Test
    @DisplayName("Get customer by ID")
    public void testGetCustomerById_whenValidIdProvided_returnsCustomer() throws Exception {
        CustomerDto customer = getCustomer();
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customers/1")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        CustomerDto retrievedCustomer = new ObjectMapper().readValue(responseBodyAsString, CustomerDto.class);
        Assertions.assertEquals(customer.getId(), retrievedCustomer.getId(), "Retrieved Customer ID is incorrect");
        Assertions.assertEquals(customer.getName(), retrievedCustomer.getName(), "Retrieved Customer name is incorrect");
    }

    @Test
    @DisplayName("Get customer by ID - Invalid ID")
    public void testGetCustomerById_whenInvalidIdProvided_returnsNotFound() throws Exception {
        when(customerService.getCustomerById(999L)).thenThrow(new ResourceNotFoundException("Customer not found"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customers/999")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
        Assertions.assertEquals(404, mvcResult.getResponse().getStatus(), "Response status should be 404 Not Found");
    }

    @Test
    @DisplayName("Delete customer by ID")
    public void testDeleteCustomerById_whenValidIdProvided_returnsNoContent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/customers/1")
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(status().isNoContent()).andReturn();
        Assertions.assertEquals(204, mvcResult.getResponse().getStatus(), "Response status should be 204 No Content");
    }

    @Test
    @DisplayName("Create customer with invalid data")
    public void testCreateCustomer_whenInvalidDetailsProvided_returnsBadRequest() throws Exception {
        CustomerDto customer = new CustomerDto();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customers")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(asJson(customer));

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
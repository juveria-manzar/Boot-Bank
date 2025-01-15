package com.juveriatech.demo.controller;

import com.juveriatech.demo.dto.AccountDto;
import com.juveriatech.demo.exception.BadRequestException;
import com.juveriatech.demo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/customers/{customerId}/accounts")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto, @PathVariable (required = true) Long customerId) {
        if (customerId == null) {
            throw new BadRequestException("Customer ID must not be null");
        }
        return new ResponseEntity<>(accountService.createAccount(accountDto, customerId), HttpStatus.CREATED);
    }


    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable (required = true) Long accountId) {
        if(accountId == null){
            throw new BadRequestException("Account ID must not be null");
        }
        return new ResponseEntity<>(accountService.getAccountById(accountId), HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable (required = true) Long accountId) {
        if(accountId == null){
            throw new BadRequestException("Account ID must not be null");
        }
        accountService.deleteAccountById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

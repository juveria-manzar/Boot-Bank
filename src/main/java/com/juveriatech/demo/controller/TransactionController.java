package com.juveriatech.demo.controller;

import com.juveriatech.demo.dto.APIResponseList;
import com.juveriatech.demo.dto.TransactionDto;
import com.juveriatech.demo.exception.BadRequestException;
import com.juveriatech.demo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RequestMapping("/api/accounts")
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<?> makeTransaction(@Valid @RequestBody TransactionDto transactionDto, @PathVariable(required = true) Long accountId, BindingResult result) {
        if (accountId == null) {
            throw new BadRequestException("Account Id must not be null");
        }
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(transactionService.makeTransaction(accountId, transactionDto));
    }
    @GetMapping("/{accountId}/transactions")
    public APIResponseList<Page<TransactionDto>> getCustomerTransactions(@PathVariable(required = true) Long accountId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        if (accountId == null) {
            throw new BadRequestException("Account Id must not be null");
        }
        Page<TransactionDto> allTransactions = transactionService.getCustomerTransactions(accountId, page, size);
        return new APIResponseList<>(allTransactions.getSize (), allTransactions);
    }
}
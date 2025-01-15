package com.juveriatech.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDto {

    private Long Id;

    @NotNull(message = "Account number cannot be blank")
    private Long accountNumber;

    @Min(value = 2000, message = "Balance must be greater than 2000.")
    private double balance;

    private Long customerId;
}

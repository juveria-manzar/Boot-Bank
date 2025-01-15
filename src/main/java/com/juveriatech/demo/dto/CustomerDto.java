package com.juveriatech.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {


    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private AccountDto account;
}

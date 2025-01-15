package com.juveriatech.demo.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class TransactionDto {


    private Long id;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Mode cannot be blank")
    @Pattern(regexp = "DEBIT|CREDIT", message = "Mode must be one of the following: CREDIT, DEBIT")
    private String mode;

    private LocalDateTime transactionDate;

    private Long accountId;
}

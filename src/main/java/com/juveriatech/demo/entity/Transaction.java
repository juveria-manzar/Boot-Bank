package com.juveriatech.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="transactions")
@Entity
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String mode;

    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name="account_id", nullable = false)
    private Account account;


}

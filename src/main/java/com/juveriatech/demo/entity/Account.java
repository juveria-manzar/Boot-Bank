package com.juveriatech.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="accounts", uniqueConstraints = @UniqueConstraint(columnNames = "accountNumber"))
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long accountNumber;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @Version
    private Long version;
}

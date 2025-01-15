package com.juveriatech.demo.repository;

import com.juveriatech.demo.dto.CustomerDto;
import com.juveriatech.demo.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = "account")
    Optional<Customer> findById(Long id);
}

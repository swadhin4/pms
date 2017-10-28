package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

	public Customer findByEmail(String email);
}

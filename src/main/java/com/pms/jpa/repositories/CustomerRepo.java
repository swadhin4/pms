package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

	public Customer findByEmail(String email);
}

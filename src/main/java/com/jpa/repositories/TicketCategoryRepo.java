package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.TicketCategory;



public interface TicketCategoryRepo extends JpaRepository<TicketCategory, Long>{

	
}

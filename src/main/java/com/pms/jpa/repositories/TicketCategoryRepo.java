package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketCategory;



public interface TicketCategoryRepo extends JpaRepository<TicketCategory, Long>{

	
}

package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketPrioritySettings;

public interface TicketPrioritySettingsRepo extends JpaRepository<TicketPrioritySettings, Long> {
	
	
	public TicketPrioritySettings findByTicketCategoryId(Long categoryId);
}

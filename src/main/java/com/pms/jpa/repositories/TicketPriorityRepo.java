package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketPriority;

public interface TicketPriorityRepo extends JpaRepository<TicketPriority, Long> {

}

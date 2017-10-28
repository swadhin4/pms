package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.TicketSiteView;

public interface TicketViewRepo extends JpaRepository<TicketSiteView, Long> {

}

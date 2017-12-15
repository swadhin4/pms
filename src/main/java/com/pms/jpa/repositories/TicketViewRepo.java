package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketSiteView;

public interface TicketViewRepo extends JpaRepository<TicketSiteView, Long> {

}

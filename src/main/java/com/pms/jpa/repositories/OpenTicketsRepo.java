package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.OpenTicketsView;

public interface OpenTicketsRepo extends JpaRepository<OpenTicketsView, Long> {

}

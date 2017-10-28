package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.OpenTicketsView;

public interface OpenTicketsRepo extends JpaRepository<OpenTicketsView, Long> {

}

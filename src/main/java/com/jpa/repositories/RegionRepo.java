package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.Region;


public interface RegionRepo extends JpaRepository<Region, Long> {

}

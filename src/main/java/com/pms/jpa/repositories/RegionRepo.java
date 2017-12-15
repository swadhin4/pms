package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.Region;


public interface RegionRepo extends JpaRepository<Region, Long> {

}

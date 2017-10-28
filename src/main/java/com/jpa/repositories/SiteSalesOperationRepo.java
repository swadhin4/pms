package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.SiteSalesOperation;

public interface SiteSalesOperationRepo extends JpaRepository<SiteSalesOperation, Long> {

	List<SiteSalesOperation> findBySiteSiteId(Long siteId);

}

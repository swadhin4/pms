package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.SiteSalesOperation;

public interface SiteSalesOperationRepo extends JpaRepository<SiteSalesOperation, Long> {

	List<SiteSalesOperation> findBySiteSiteId(Long siteId);

}

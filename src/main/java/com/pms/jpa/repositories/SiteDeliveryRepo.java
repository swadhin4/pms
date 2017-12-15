package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.SiteDeliveryOperation;

public interface SiteDeliveryRepo extends JpaRepository<SiteDeliveryOperation, Long> {

	List<SiteDeliveryOperation> findBySiteSiteId(Long siteId);

}

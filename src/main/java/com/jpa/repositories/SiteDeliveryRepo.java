package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.SiteDeliveryOperation;

public interface SiteDeliveryRepo extends JpaRepository<SiteDeliveryOperation, Long> {

	List<SiteDeliveryOperation> findBySiteSiteId(Long siteId);

}

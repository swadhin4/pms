package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.SiteSubMeter;

public interface SiteSubmeterRepo extends JpaRepository<SiteSubMeter, Long> {

	List<SiteSubMeter> findBySiteSiteId(Long siteId);

}

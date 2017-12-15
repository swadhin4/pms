package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.SiteSubMeter;

public interface SiteSubmeterRepo extends JpaRepository<SiteSubMeter, Long> {

	List<SiteSubMeter> findBySiteSiteId(Long siteId);

}

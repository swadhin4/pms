package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.SiteLicence;

public interface LicenseRepo extends JpaRepository<SiteLicence, Long> {

	public List<SiteLicence> findBySiteSiteId(Long siteId);

}

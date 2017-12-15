package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.SiteLicence;

public interface LicenseRepo extends JpaRepository<SiteLicence, Long> {

	public List<SiteLicence> findBySiteSiteId(Long siteId);
	
	public List<SiteLicence> findByLicenseIdIn(List<Long> licenseIds);

}

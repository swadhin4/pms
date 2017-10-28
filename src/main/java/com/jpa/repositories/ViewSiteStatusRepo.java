package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.ViewSiteStatus;

public interface ViewSiteStatusRepo extends JpaRepository<ViewSiteStatus, Long> {

	public List<ViewSiteStatus> findBySiteId(Long siteId);
}

package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.ViewSitePriority;
import com.jpa.entities.ViewSiteStatus;

public interface ViewSitePriorityRepo extends JpaRepository<ViewSitePriority, Long> {

	public List<ViewSitePriority> findBySiteIdIn(List<Long> siteId);
}

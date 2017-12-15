package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.ViewSitePriority;

public interface ViewSitePriorityRepo extends JpaRepository<ViewSitePriority, Long> {

	public List<ViewSitePriority> findBySiteIdIn(List<Long> siteId);
}

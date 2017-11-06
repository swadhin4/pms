package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.ViewSPPriority;

public interface ViewSPPriorityRepo extends JpaRepository<ViewSPPriority, Long> {
	
	public List<ViewSPPriority> findBySpIdIn(List<Long> spId);

}

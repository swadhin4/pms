package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.ViewSPStatus;

public interface ViewSPStatusRepo extends JpaRepository<ViewSPStatus, Long> {

	public List<ViewSPStatus> findBySpIdIn(List<Long> spList);

}

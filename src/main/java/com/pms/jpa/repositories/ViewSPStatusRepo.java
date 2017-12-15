package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.ViewSPStatus;

public interface ViewSPStatusRepo extends JpaRepository<ViewSPStatus, Long> {

	public List<ViewSPStatus> findBySpIdIn(List<Long> spList);

}

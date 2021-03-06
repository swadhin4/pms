package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.jpa.entities.Status;

public interface StatusRepo extends JpaRepository<Status, Long> {


	@Query("from Status s where s.category=:category")
	public List<Status> findByStatusCategory(@Param(value="category") String category);
}

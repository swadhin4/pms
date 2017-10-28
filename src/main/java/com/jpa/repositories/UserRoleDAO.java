/*
 * Copyright (C) 2013 , Inc. All rights reserved 
 */
package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.User;
import com.jpa.entities.UserRole;

/**
 * The Interface UserRoleDAO.
 * 
 * 
 */
public interface UserRoleDAO extends JpaRepository<UserRole, Long> {

	public List<UserRole> findByUser(User user);
}

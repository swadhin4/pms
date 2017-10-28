/*
 * Copyright (C) 2013 , Inc. All rights reserved 
 */
package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jpa.entities.Role;

/**
 * The Interface RoleDAO.
 * 
 * 
 */
public interface RoleDAO extends JpaRepository<Role, Long> {

	@Query("from Role r where r.roleName = 'ROLE_SP_EXTERNAL'")
	public Role findRoleByName();

}

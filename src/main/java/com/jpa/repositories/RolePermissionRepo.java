package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.entities.RolePermission;

public interface RolePermissionRepo extends JpaRepository<RolePermission, Long> {


	@Query("from RolePermission rp where rp.role.roleId=:roleId")
	public List<RolePermission> findPermissionsByRole(@Param(value="roleId") Long roleId) throws Exception;
}

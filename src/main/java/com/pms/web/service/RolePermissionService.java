package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.AppFeature;
import com.pms.jpa.entities.UserRole;

public interface RolePermissionService {

	public List<AppFeature> getUserFeatureAccess(UserRole loggedInUserRole) throws Exception;
}

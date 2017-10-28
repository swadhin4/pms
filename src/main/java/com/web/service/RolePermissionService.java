package com.web.service;

import java.util.List;

import com.jpa.entities.AppFeature;
import com.jpa.entities.UserRole;

public interface RolePermissionService {

	public List<AppFeature> getUserFeatureAccess(UserRole loggedInUserRole) throws Exception;
}

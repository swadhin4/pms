package com.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.entities.AppFeature;
import com.jpa.entities.RolePermission;
import com.jpa.entities.UserRole;
import com.jpa.repositories.RolePermissionRepo;
import com.web.service.RolePermissionService;

@Service("rolePersmissionService")
public class RolePermissionServiceImpl implements RolePermissionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

	@Autowired
	private RolePermissionRepo rolePermissionRepo;

	@Override
	@Transactional
	public List<AppFeature> getUserFeatureAccess(final UserRole loggedInUserRole) throws Exception {
		List<AppFeature> appAccessList = new ArrayList<AppFeature>();
		List<RolePermission> rolePermissions = rolePermissionRepo.findPermissionsByRole(loggedInUserRole.getRole().getRoleId());
		if(!rolePermissions.isEmpty()){
			for(RolePermission rolePermission:rolePermissions) {
				AppFeature appFeature = rolePermission.getAppFeature();
				LOGGER.info("User has access : "+ rolePermission + " to feature : "+  appFeature.getFeatureName());
				appAccessList.add(appFeature);
			}
		}
		return appAccessList;
	}

}

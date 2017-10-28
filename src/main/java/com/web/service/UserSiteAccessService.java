package com.web.service;

import java.util.List;

import com.jpa.entities.UserSiteAccess;
import com.pmsapp.view.vo.LoginUser;
import com.pmsapp.view.vo.UserSiteAccessVO;

public interface UserSiteAccessService {

	public List<UserSiteAccess> getUserSiteAccess(Long userId) throws Exception;

	public List<UserSiteAccessVO> getUserSiteAccessBySite(Long siteId, LoginUser user) throws Exception;

	public UserSiteAccessVO assignUserToSite(Long userId, Long siteId) throws Exception;

	public boolean removeUserAccessFromSite(Long accessId) throws Exception;
}

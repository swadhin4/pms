package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.UserSiteAccessVO;
import com.pms.jpa.entities.UserSiteAccess;

public interface UserSiteAccessService {

	public List<UserSiteAccess> getUserSiteAccess(Long userId) throws Exception;

	public List<UserSiteAccessVO> getUserSiteAccessBySite(Long siteId, LoginUser user) throws Exception;

	public UserSiteAccessVO assignUserToSite(Long userId, Long siteId) throws Exception;

	public boolean removeUserAccessFromSite(Long accessId) throws Exception;
}

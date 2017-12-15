/*
 */
package com.pms.app.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.UserRole;
import com.pms.web.service.CompanyService;
import com.pms.web.service.RolePermissionService;
import com.pms.web.service.UserService;
import com.pms.web.service.security.AuthorizedUserDetails;
import com.pms.web.util.CredentialUtils;
import com.pms.web.util.ServiceUtil;

/**
 * The Class BaseController.
 *
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);


	/** The user service. */
	@Autowired
	protected UserService userService;
	
	@Autowired
	private CompanyService companyService;


	@Autowired
	protected RolePermissionService rolePermissionService;

	@Autowired
	protected  UserDetailsService  userAuthorizationService;
	/**
	 * Gets the current loggedin user name.
	 *
	 * @return the current loggedin user name
	 */
	public String getCurrentLoggedinUserName() {
		logger.info("Getting current logged in Username");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if(StringUtils.isNotBlank(username)){
			return username;
		}
		return username;
	}

	/**
	 * Gets the current loggedin user.
	 *
	 * @return the current loggedin user
	 */
	public LoginUser getCurrentLoggedinUser(final HttpSession session) {
		logger.info("Inside BaseController - getCurrentLoggedinUser");
		logger.info("Getting user from session..");
		LoginUser loginUser=(LoginUser) session.getAttribute("loginUser");
		if(loginUser==null){
			logger.info("User not available in session.");
			String username = getCurrentLoggedinUserName();
			if (StringUtils.isNotBlank(username)) {
				logger.info("Getting User credentials for username : " + username);
				if(username.equalsIgnoreCase("anonymousUser")){
					loginUser=new LoginUser();
					loginUser.setLoginStatus(0);
				}else{
					Authentication springAuthentication = SecurityContextHolder.getContext().getAuthentication();
					AuthorizedUserDetails authUser = null;
					try {
						authUser = userService.getAuthorizedUser(springAuthentication);
						if(authUser.getUser()!=null) {
							if(authUser.getUser().getEnabled() == 0){
								logger.info("User account is locked");
								loginUser=new LoginUser();
								loginUser.setLoginStatus(404);
								loginUser.setMessage("Please verify your credentials. Account is either locked or not created. Please contact administrator.");
							}else{
								loginUser=new LoginUser();
								logger.info("User found for " + username);
								loginUser.setUserId(authUser.getUser().getUserId());
								loginUser.setUserRoles(authUser.getUser().getUserRoles());
								loginUser.setFirstName(authUser.getUser().getFirstName());
								loginUser.setLastName(authUser.getUser().getLastName());
								loginUser.setUsername(authUser.getUser().getEmailId());
								loginUser.setSysPassword(authUser.getUser().getSysPassword());
								loginUser.setCompany(authUser.getUser().getCompany());
								loginUser.setPhoneNo(String.valueOf(authUser.getUser().getPhone()));
								UserRole loggedInUserRole = loginUser.getUserRoles().get(0);
								/*logger.info("Getting list of persmission for the user role : "+ loggedInUserRole.getRole().getRoleName());
								try{
									List<AppFeature> featureAccessList = rolePermissionService.getUserFeatureAccess(loggedInUserRole);
									if(!featureAccessList.isEmpty()){
										loginUser.setFeatureList(featureAccessList);
									}

								}catch(Exception e){
									logger.error("Exception while getting user details ", e);
								}*/
								session.setAttribute("loginUser", loginUser);
							}
						}else{
							logger.info("User not found : " + username);
							AuthorizedUserDetails authUser2 = (AuthorizedUserDetails)springAuthentication.getPrincipal();
							System.out.println(authUser2.isAccountNonLocked());
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		}

		logger.info("Exit BaseController - getCurrentLoggedinUser");
		return loginUser;
	}


	public void setResponseWithFile(final String fileName, final HttpServletResponse response) throws IOException {

		File file = new File(fileName);
		response.setContentType(ServiceUtil.getImageMimeType(file.getName()));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		BufferedInputStream input = null;

		try {
			input = new BufferedInputStream(new FileInputStream(file));
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.debug("Exception while file cop", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ignore) {}
			}
		}
	}


	private AuthorizedUserDetails authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = getCurrentLoggedinUserName();
		String password = String.valueOf(auth.getCredentials());

		logger.info("username:" + username);
		logger.info("password:" + password); // Don't log passwords in real app

		// 1. Use the username to load the data for the user, including authorities and password.
		// 2. Check the passwords match (should use a hashed password here).
		AuthorizedUserDetails userDetails = (AuthorizedUserDetails) userAuthorizationService.loadUserByUsername(username);
		try {
			boolean isPasswordValidated = CredentialUtils.validatePassword(password, userDetails.getUser().getPassword());
			if (isPasswordValidated) {

				// 3. Preferably clear the password in the user object before storing in authentication object
				userDetails.eraseCredentials();

				// 4. Return an authenticated token, containing user data and authorities  
				userDetails.getUser().setPassword("");
			}
			else{
				throw new BadCredentialsException("Bad Credentials");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.info("Exception while validating password", e);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			logger.info("Exception while validating key for the password", e);
		}
		return userDetails ;
	}

}

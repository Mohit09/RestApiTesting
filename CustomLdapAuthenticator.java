package com.sbic.turbine.security;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sbic.turbine.exception.TurbineApiException;
import com.sbic.turbine.exception.UnauthorisedException;
import com.sbic.turbine.security.services.LdapUserServiceImpl;

/**
 * Class to provide customised Ldap Authentication, by using LDAP authentication
 * properties.
 * 
 * @author 600010083
 *
 */
@Component
public class CustomLdapAuthenticator {
	private static final Logger log = LoggerFactory.getLogger(CustomLdapAuthenticator.class);
	@Autowired
	private LdapUserServiceImpl ldapManager;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("Authetication of user details started.");
		ServletRequestAttributes authContext = ((ServletRequestAttributes) (RequestContextHolder
				.currentRequestAttributes()));

		String username = authContext.getRequest().getParameter("username");// authentication.getName();
		String password = authContext.getRequest().getParameter("password");// (String) authentication.getCredentials();
		boolean userAttribFetched = ldapManager.authenticateUser(username, password);
		log.info("Authetication Done for the user: " + username);
		if (!userAttribFetched)
			throw new UnauthorisedException("Invalid Credentials: " + username);

		return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
	}

	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}

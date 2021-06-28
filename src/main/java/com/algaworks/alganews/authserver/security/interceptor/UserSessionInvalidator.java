package com.algaworks.alganews.authserver.security.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSessionInvalidator {
	
	private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
	private final UserDetailsService userDetailsService;
	
	public void invalidateSessionIfNecessary(String sessionId, String userName) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		if (!userDetails.isEnabled())
			invalidateSession(sessionId);
	}
	
	private void invalidateSession(String sessionId) {
		sessionRepository.deleteById(sessionId);
		throw new UnauthorizedUserException("Usuário desabilitado");
	}
}

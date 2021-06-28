package com.algaworks.alganews.authserver.security.handler;

import com.algaworks.alganews.authserver.core.SecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class RedirectOnLogoutSuccessHandler implements LogoutSuccessHandler {
	
	private final SecurityProperties securityProperties;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
								HttpServletResponse response,
								Authentication authentication)
			throws IOException, ServletException {
		response.sendRedirect(getRedirectUrl(request));
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
	}
	
	private String getRedirectUrl(HttpServletRequest request) {
		if (request.getParameterMap() != null)
			return request.getParameterMap()
					.getOrDefault("redirect", new String[]{securityProperties.getLogoutDefaultRedirectUrl()})[0];
			
		return securityProperties.getLogoutDefaultRedirectUrl();
	}
}

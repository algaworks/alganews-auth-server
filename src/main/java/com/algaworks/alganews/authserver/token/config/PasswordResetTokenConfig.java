package com.algaworks.alganews.authserver.token.config;

import com.algaworks.alganews.authserver.core.SecurityProperties;
import com.algaworks.alganews.authserver.token.infrastructure.StatelessPasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class PasswordResetTokenConfig {
	
	private final SecurityProperties securityProperties;
	
	@Bean
	public StatelessPasswordResetTokenService passwordUpdate() {
		return new StatelessPasswordResetTokenService(securityProperties.getResetToken().getPasswordReset());
	}
	
}

package com.algaworks.alganews.authserver.core;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("alganews.security")
public class SecurityProperties {

	private boolean disabled = false;
	private Long defaultUserIdIfDisabled = 1L;
	private String logoutDefaultRedirectUrl = "/";
	
	@Valid
	private ResetTokenProperties resetToken = new ResetTokenProperties();
	
	@Getter
	@Setter
	public class ResetTokenProperties {
		
		@Valid
		private PasswordResetProperties passwordReset = new PasswordResetProperties();
		
	}
	
	@Getter
	@Setter
	public class PasswordResetProperties implements TokenProperties {

		@URL
		@NotEmpty
		private String url;
		
		@NotEmpty
		private String secret;
		
		@NotNull
		private Duration timeout;
		
	}
	
	public interface TokenProperties {
		
		String getUrl();
		String getSecret();
		Duration getTimeout();
		
	}
	
}

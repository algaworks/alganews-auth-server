package com.algaworks.alganews.authserver.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
@Component
@ConfigurationProperties("alganews.auth")
public class AuthProperties {

	@Valid
	private Clients clients = new Clients();

	@NotNull
	private Duration accessTokenValidity = Duration.ofMinutes(5);
	
	@NotNull
	private Duration refreshTokenValidity = Duration.ofDays(1);
	
	@NotEmpty
	private String tokensSigningKey;
	
	public int getAccessTokenValidityInSeconds() {
		return Long.valueOf(getAccessTokenValidity().getSeconds()).intValue();
	}
	
	public int getRefreshTokenValidityInSeconds() {
		return Long.valueOf(getRefreshTokenValidity().getSeconds()).intValue();
	}
	
	public String getSwaggerUiClientSecret() {
		return getClients().getSwaggerUi().getSecret();
	}
	
	public String[] getCmsClientCallbackUrls() {
		return getClients().getCms().getCallbackUrls();
	}
	
	public String[] getAdminClientCallbackUrls() {
		return getClients().getAdmin().getCallbackUrls();
	}

	@Data
	public class Clients {
		
		@Valid
		private CmsClient cms = new CmsClient();
		
		@Valid
		private AdminClient admin = new AdminClient();
		
		@Valid
		private SwaggerUiClient swaggerUi = new SwaggerUiClient();
		
	}
	
	@Data
	public class SwaggerUiClient {
		
		@NotNull
		private String secret;
		
	}
	
	@Data
	public class CmsClient {
		
		@NotEmpty
		private String[] callbackUrls;
		
	}
	
	@Data
	public class AdminClient {
		
		@NotEmpty
		private String[] callbackUrls;
		
	}
	
}

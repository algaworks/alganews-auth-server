package com.algaworks.alganews.authserver.token.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.OffsetDateTime;

public interface PasswordResetTokenService {
	
	String makeToken(TokenPayload tokenPayload);
	
	boolean isExpired(String token);
	
	boolean isValid(TokenPayload tokenPayload, String token);
	
	default boolean isInvalid(TokenPayload tokenPayload, String token) {
		return !isValid(tokenPayload, token);
	}
	
	String getResetUrl();
	
	String extractUsername(String token);
	
	@Builder
	@Getter
	class TokenPayload {
	
		@NonNull
		private Long userId;
		
		@NonNull
		private String email;
		
		private String password;
		
		private OffsetDateTime lastLoginAt;
		
	}
}

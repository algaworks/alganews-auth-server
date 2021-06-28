package com.algaworks.alganews.authserver.password.converter;

import com.algaworks.alganews.authserver.token.domain.PasswordResetTokenService;
import com.algaworks.alganews.authserver.user.domain.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToTokenPayloadConverter implements Converter<UserEntity, PasswordResetTokenService.TokenPayload> {

	@Override
	public PasswordResetTokenService.TokenPayload convert(UserEntity user) {
		return PasswordResetTokenService.TokenPayload.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.password(user.getPassword())
			.lastLoginAt(user.getLastLogin())
			.build();
	}

}

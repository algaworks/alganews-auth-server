package com.algaworks.alganews.authserver.password.service;

import com.algaworks.alganews.authserver.common.exception.BusinessException;
import com.algaworks.alganews.authserver.password.exception.ExpiredTokenException;
import com.algaworks.alganews.authserver.password.exception.InvalidTokenException;
import com.algaworks.alganews.authserver.token.domain.PasswordResetTokenService;
import com.algaworks.alganews.authserver.user.domain.UserEntity;
import com.algaworks.alganews.authserver.user.service.UserCrudService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserPasswordUpdateService {

	private final PasswordResetTokenService passwordResetTokenService;
	private final UserCrudService userCrudService;
	private final PasswordEncoder passwordEncoder;
	private final ConversionService conversionService;
	
	@Transactional
	public void updateUsingToken(String newPassword, String token) throws InvalidTokenException {
		if (StringUtils.isBlank(newPassword)) {
			throw new BusinessException("A nova senha não pode estar em branco.");
		}
		
		UserEntity user = userCrudService.findByEmailOrFail(passwordResetTokenService.extractUsername(token));
		
		var tokenPayload = conversionService.convert(user, PasswordResetTokenService.TokenPayload.class);
		
		if (passwordResetTokenService.isExpired(token)) {
			throw new ExpiredTokenException();
		}
		
		if (passwordResetTokenService.isInvalid(tokenPayload, token)) {
			throw new InvalidTokenException();
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
	}
	
	public void checkExpiration(String token) throws InvalidTokenException {
		if (passwordResetTokenService.isExpired(token)) {
			throw new ExpiredTokenException();
		}
	}
	
}

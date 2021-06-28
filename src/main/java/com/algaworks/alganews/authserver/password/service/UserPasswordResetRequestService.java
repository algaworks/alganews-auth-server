package com.algaworks.alganews.authserver.password.service;

import com.algaworks.alganews.authserver.email.domain.EmailSenderService;
import com.algaworks.alganews.authserver.token.domain.PasswordResetTokenService;
import com.algaworks.alganews.authserver.token.domain.PasswordResetTokenService.TokenPayload;
import com.algaworks.alganews.authserver.user.domain.UserEntity;
import com.algaworks.alganews.authserver.user.service.UserCrudService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPasswordResetRequestService {

	private final PasswordResetTokenService passwordResetTokenService;
	private final UserCrudService userCrudService;
	private final EmailSenderService emailSenderService;
	private final ConversionService conversionService;
	
	public void requestResetting(String userEmail) {
		UserEntity user = userCrudService.findByEmailOrFail(userEmail);
		
		var tokenPayload = conversionService.convert(user, TokenPayload.class);
		
		String token = passwordResetTokenService.makeToken(tokenPayload);
		sendEmail(user, token);
	}
	
	private void sendEmail(UserEntity user, String token) {
		var message = EmailSenderService.Message.builder()
				.subject("AlgaNews - Redefinição de senha")
				.body("users/user-password-reset.html")
				.data("userName", user.getName())
				.data("resetUrl", makeUrl(user.getId(), token))
				.to(user.getEmail())
				.build();
		
		emailSenderService.send(message);
	}
	
	private String makeUrl(Long userId, String token) {
		return passwordResetTokenService.getResetUrl()
				.replace("{token}", token);
	}
	
}

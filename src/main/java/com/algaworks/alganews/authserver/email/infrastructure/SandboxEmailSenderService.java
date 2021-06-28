package com.algaworks.alganews.authserver.email.infrastructure;

import com.algaworks.alganews.authserver.email.config.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SandboxEmailSenderService extends SmtpEmailSenderService {

	@Autowired
	private EmailProperties emailProperties;
	
	@Override
	protected MimeMessage createMimeMessage(Message message) throws MessagingException {
		MimeMessage mimeMessage = super.createMimeMessage(message);
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		helper.setTo(emailProperties.getSandbox().getTo());
		
		return mimeMessage;
	}

}

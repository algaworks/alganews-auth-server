package com.algaworks.alganews.authserver.email.config;

import com.algaworks.alganews.authserver.email.domain.EmailSenderService;
import com.algaworks.alganews.authserver.email.infrastructure.FakeEmailSenderService;
import com.algaworks.alganews.authserver.email.infrastructure.SandboxEmailSenderService;
import com.algaworks.alganews.authserver.email.infrastructure.SmtpEmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

	@Autowired
	private EmailProperties emailProperties;

	@Bean
	public EmailSenderService envioEmailService() {
		switch (emailProperties.getImpl()) {
			case FAKE:
				return new FakeEmailSenderService();
			case SMTP:
				return new SmtpEmailSenderService();
			case SANDBOX:
				return new SandboxEmailSenderService();
			default:
				return null;
		}
	}

}
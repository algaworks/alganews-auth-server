package com.algaworks.alganews.authserver.email.infrastructure;

import com.algaworks.alganews.authserver.email.domain.EmailSenderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeEmailSenderService extends SmtpEmailSenderService {

	@Override
	public void send(EmailSenderService.Message message) {
		String body = processTemplate(message);

		log.info("[FAKE E-MAIL] To: {}\n{}", message.getRecipient(), body);
	}

}

package com.algaworks.alganews.authserver.password.exception;

import com.algaworks.alganews.authserver.common.exception.BusinessException;

public class ExpiredTokenException extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	public ExpiredTokenException() {
		super("O token de redefinição de senha expirou");
	}
	
}

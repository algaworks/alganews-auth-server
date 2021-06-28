package com.algaworks.alganews.authserver.password.exception;

import com.algaworks.alganews.authserver.common.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
		super("O token de redefinição de senha não é válido");
	}
	
}

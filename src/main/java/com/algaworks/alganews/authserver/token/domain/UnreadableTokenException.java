package com.algaworks.alganews.authserver.token.domain;

public class UnreadableTokenException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UnreadableTokenException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

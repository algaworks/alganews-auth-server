package com.algaworks.alganews.authserver.password.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordUpdateRequestInput {
	
	@Email
	@NotBlank
	private String email;
	
}

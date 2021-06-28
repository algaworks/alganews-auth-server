package com.algaworks.alganews.authserver.password.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserPasswordUpdateInput {
	
	@NotBlank
	private String password;
	
	
}

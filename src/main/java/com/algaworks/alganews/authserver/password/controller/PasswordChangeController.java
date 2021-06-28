package com.algaworks.alganews.authserver.password.controller;

import com.algaworks.alganews.authserver.password.exception.ExpiredTokenException;
import com.algaworks.alganews.authserver.password.exception.InvalidTokenException;
import com.algaworks.alganews.authserver.password.model.UserPasswordUpdateInput;
import com.algaworks.alganews.authserver.password.service.UserPasswordUpdateService;
import com.algaworks.alganews.authserver.token.domain.UnreadableTokenException;
import com.algaworks.alganews.authserver.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping
public class PasswordChangeController {
	
	private final UserPasswordUpdateService userPasswordUpdateService;
	
	@GetMapping("/change-password/{token}")
	public String updateUsingToken(@PathVariable("token") String token,
								   Model model, UserPasswordUpdateInput input) {
		model.addAttribute("token", token);
		model.addAttribute("input", input);
		
		try {
			userPasswordUpdateService.checkExpiration(token);
		} catch (ExpiredTokenException | UnreadableTokenException e) {
			model.addAttribute("msg", "Token inválido.");
			return "pages/error";
		}
		
		return "pages/change-password";
	}
	
	@PostMapping("/change-password/{token}")
	public String updateUsingTokenSave(@PathVariable("token") String token,
									   Model model,
									   @Valid UserPasswordUpdateInput input,
									   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return updateUsingToken(token, model, input);
		}
		
		try {
			userPasswordUpdateService.updateUsingToken(input.getPassword(), token);
		} catch (UserNotFoundException | ExpiredTokenException | InvalidTokenException e) {
			model.addAttribute("msg", "Token inválido.");
			return "pages/error";
		}
		
		model.addAttribute("msg", "Utilize sua nova senha para realizar o login.");
		return "pages/success";
	}

}

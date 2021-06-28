package com.algaworks.alganews.authserver.password.controller;

import com.algaworks.alganews.authserver.password.model.PasswordUpdateRequestInput;
import com.algaworks.alganews.authserver.password.service.UserPasswordResetRequestService;
import com.algaworks.alganews.authserver.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/forget-password")
public class PasswordUpdateRequestController {
	
	private final UserPasswordResetRequestService userPasswordResetRequestService;
	
	@GetMapping
	public String checkExpiration(Model model) {
		return "pages/forget-password";
	}
	
	@PostMapping
	public String requestResetting(Model model,
								   @Valid PasswordUpdateRequestInput input,
								   BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return checkExpiration(model);
		}
		
		try {
			userPasswordResetRequestService.requestResetting(input.getEmail());
		} catch (UserNotFoundException e) {
			model.addAttribute("msg", "Usuário não encontrado.");
			return "pages/error";
		}
		
		model.addAttribute("msg", "Um link de verficação foi enviado para este e-mail.");
		return "pages/success";
	}

}

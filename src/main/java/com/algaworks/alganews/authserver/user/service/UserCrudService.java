package com.algaworks.alganews.authserver.user.service;

import com.algaworks.alganews.authserver.user.domain.UserEntity;
import com.algaworks.alganews.authserver.user.exception.UserNotFoundException;
import com.algaworks.alganews.authserver.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserCrudService {
	
	private final UserRepository userRepository;
	
	public UserEntity findByEmailOrFail(String email) {
		return userRepository.findActivesByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
	}
	
	@Transactional
	public UserEntity save(UserEntity user) {
		user = userRepository.save(user);
		return user;
	}
	
}

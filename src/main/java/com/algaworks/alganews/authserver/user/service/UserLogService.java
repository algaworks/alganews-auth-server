package com.algaworks.alganews.authserver.user.service;

import com.algaworks.alganews.authserver.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class UserLogService {

	private final UserCrudService userCrudService;
	
	public void recordUserLogin(String userName) {
		UserEntity userEntity = userCrudService.findByEmailOrFail(userName);
		userEntity.setLastLogin(OffsetDateTime.now());
		userCrudService.save(userEntity);
	}

}

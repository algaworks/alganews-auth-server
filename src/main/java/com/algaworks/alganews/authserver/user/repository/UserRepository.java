package com.algaworks.alganews.authserver.user.repository;

import com.algaworks.alganews.authserver.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("from UserEntity where email = :email and active = true")
	Optional<UserEntity> findActivesByEmail(String email);
	
}

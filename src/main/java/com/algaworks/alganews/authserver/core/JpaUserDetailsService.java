package com.algaworks.alganews.authserver.core;

import com.algaworks.alganews.authserver.user.domain.UserEntity;
import com.algaworks.alganews.authserver.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

	private final UserRepository users;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity usuario = users.findActivesByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail informado"));
		
		return new AuthUser(usuario, getAuthorities(usuario));
	}
	
	private Collection<GrantedAuthority> getAuthorities(UserEntity user) {
		return Stream.of(user.getRole())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}

}

package com.algaworks.alganews.authserver.config;

import com.algaworks.alganews.authserver.security.handler.RedirectOnLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Autowired
	private RedirectOnLogoutSuccessHandler redirectOnLogoutSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().
				loginPage("/login")
				.and()
				.csrf().disable()
				.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutUrl("/logout")
				.logoutSuccessHandler(redirectOnLogoutSuccessHandler)
				.logoutSuccessUrl("/login");
	}
	
}
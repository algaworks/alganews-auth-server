package com.algaworks.alganews.authserver.config;

import com.algaworks.alganews.authserver.core.AuthProperties;
import com.algaworks.alganews.authserver.core.JwtCustomClaimsTokenEnhancer;
import com.algaworks.alganews.authserver.core.PkceAuthorizationCodeTokenGranter;
import com.algaworks.alganews.authserver.security.interceptor.UserSessionInvalidatorRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private static final String SCOPE_ALL_WRITE = "all:write";
	private static final String SCOPE_ALL_READ = "all:read";
	private static final String SCOPE_POST_READ = "post:read";
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthProperties authProperties;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserSessionInvalidatorRequestInterceptor userSessionInvalidatorRequestInterceptor;
	
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
				.withClient("alganews-cms")
				.secret(passwordEncoder.encode(""))
				.authorizedGrantTypes("authorization_code", "refresh_token")
				.scopes(SCOPE_ALL_WRITE, SCOPE_ALL_READ, SCOPE_POST_READ)
				.accessTokenValiditySeconds(authProperties.getAccessTokenValidityInSeconds())
				.refreshTokenValiditySeconds(authProperties.getRefreshTokenValidityInSeconds())
				.redirectUris(authProperties.getCmsClientCallbackUrls())
				.autoApprove(true)
			.and()
				.withClient("alganews-admin")
				.secret(passwordEncoder.encode(""))
				.authorizedGrantTypes("authorization_code", "refresh_token")
				.scopes(SCOPE_ALL_WRITE, SCOPE_ALL_READ, SCOPE_POST_READ)
				.accessTokenValiditySeconds(authProperties.getAccessTokenValidityInSeconds())
				.refreshTokenValiditySeconds(authProperties.getRefreshTokenValidityInSeconds())
				.redirectUris(authProperties.getAdminClientCallbackUrls())
				.autoApprove(true)
			.and()
				.withClient("swagger-ui")
				.secret(passwordEncoder.encode(authProperties.getSwaggerUiClientSecret()))
				.authorizedGrantTypes("password")
				.scopes(SCOPE_ALL_WRITE, SCOPE_ALL_READ)
				.accessTokenValiditySeconds(authProperties.getAccessTokenValidityInSeconds());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients();
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		var enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter()));
		
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.tokenEnhancer(enhancerChain)
			.reuseRefreshTokens(false)
			.accessTokenConverter(jwtAccessTokenConverter())
			.authorizationCodeServices(authorizationCodeServices())
			.addInterceptor(userSessionInvalidatorRequestInterceptor)
			.tokenGranter(tokenGranter(endpoints));
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		var jwtAccessTokenConverter = new JwtAccessTokenConverter();
		
		jwtAccessTokenConverter.setSigningKey(authProperties.getTokensSigningKey());
		return jwtAccessTokenConverter;
	}
	
	private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(
				endpoints.getTokenServices(),
				endpoints.getAuthorizationCodeServices(),
				endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory(),
				userDetailsService);
		
		var granters = Arrays.asList(
				pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
		
		return new CompositeTokenGranter(granters);
	}
	
}

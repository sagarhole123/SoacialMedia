package com.social.media.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.social.media.repository.UserRepository;
import com.social.media.service.UserDetailsServiceImpl;

@Configuration
public class UserDetailsServiceConfig {

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return new UserDetailsServiceImpl(userRepository);
	}
}
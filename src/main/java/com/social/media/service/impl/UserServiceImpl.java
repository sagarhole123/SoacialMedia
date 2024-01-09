package com.social.media.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.social.media.dto.UserDto;
import com.social.media.entities.User;
import com.social.media.repository.UserRepository;
import com.social.media.service.UserService;

@Configuration
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public User registerUser(UserDto userDto) {
		// Check if the username is already taken
		if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			throw new IllegalArgumentException("Username is already taken");
		}

		// Create a new user
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setFullName(userDto.getFullName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder().encode(userDto.getPassword())); // Use BCryptPasswordEncoder

		return userRepository.save(user);
	}

	@Override
	public User updateUser(String username, UserDto userDto) throws NotFoundException {

		User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException());

		user.setUsername(userDto.getUsername());
		user.setFullName(userDto.getFullName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder().encode(userDto.getPassword())); // Use BCryptPasswordEncoder

		return userRepository.save(user);
	}

}

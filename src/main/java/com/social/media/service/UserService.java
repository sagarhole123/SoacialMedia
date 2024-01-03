package com.social.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.media.model.User;
import com.social.media.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
	UserRepository userRepository;

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public User loginUser(String username, String password) {
		User userExists = userRepository.findByusername(username);
		if (userExists == null || !userExists.getPassword().equals(password)) {
			return null; // Invalid login
		}
		return userExists;
	}
}
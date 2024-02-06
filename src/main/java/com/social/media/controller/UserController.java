package com.social.media.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social.media.dto.UserDto;
import com.social.media.entities.LoginRequest;
import com.social.media.entities.User;
import com.social.media.repository.UserRepository;
import com.social.media.security.JwtTokenProvider;
import com.social.media.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private final AuthenticationManager authenticationManager;

	// Constructor injection in UserController
	@Autowired
	public UserController(UserService userService, UserRepository userRepository,
			AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
		Optional<User> user = userRepository.findByUserName(loginRequest.getUsername());
		if (user.isPresent()) {
			try {
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
								loginRequest.getPassword()));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				LOGGER.info("Successful authentication for user: {}", loginRequest.getUsername());
				return ResponseEntity.ok("Login success");
			} catch (BadCredentialsException e) {
				LOGGER.error("Authentication failed");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Invalid credentials. Please sign up first.");
			}
		} else {
			LOGGER.error("User not found: {}");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found. Please sign up first.");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserDto user) {
		try {
			User newUser = userService.registerUser(user);
			return ResponseEntity.ok(newUser);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/update/{userName}")
	public ResponseEntity<User> updateUser(@PathVariable String userName, @RequestBody UserDto user)
			throws NotFoundException {
		User updatedUser = userService.updateUser(userName, user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/delete/{userName}")
	public ResponseEntity<String> deleteUser(@PathVariable String userName) {
		userService.deleteUser(userName);
		return ResponseEntity.ok("User Deleted Successfully");
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

}

package com.social.media.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.media.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}

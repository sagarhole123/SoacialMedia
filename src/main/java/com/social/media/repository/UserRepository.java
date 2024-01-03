package com.social.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.social.media.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByusername(String username);

}

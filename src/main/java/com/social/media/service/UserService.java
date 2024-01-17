package com.social.media.service;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.social.media.dto.UserDto;
import com.social.media.entities.User;

public interface UserService {

	User registerUser(UserDto userDto);

	User updateUser(String username, UserDto userDto) throws NotFoundException;

	void deleteUser(String userName);

	List<User>  getAllUsers();

}

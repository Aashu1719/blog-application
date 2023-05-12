package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.UserDto;
import com.blog.repositories.RoleRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private ModelMapper modelmapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User save = userRepo.save(user);
		return this.userToUserDto(save);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User usr = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));
		usr.setName(userDto.getName());
		usr.setEmail(userDto.getEmail());
		usr.setAbout(userDto.getAbout());
		usr.setPassword(userDto.getPassword());
		User save = this.userRepo.save(usr);
		return this.userToUserDto(save);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User usr = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));
		return this.userToUserDto(usr);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToUserDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		this.userRepo.delete(user);

	}

	public User dtoToUser(UserDto userDto) {
		User user = modelmapper.map(userDto, User.class);
//		User user = new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
		return user;
	}

	public UserDto userToUserDto(User user) {
		UserDto userDto = modelmapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		User user = this.modelmapper.map(userDto, User.class);
		// encode the password
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		User newUser = this.userRepo.save(user);
		return this.modelmapper.map(newUser, UserDto.class);
	}

}

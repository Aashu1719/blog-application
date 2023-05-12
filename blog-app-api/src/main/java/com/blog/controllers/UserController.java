package com.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDto;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/save")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto createUser = this.userService.createUser(userDto);
		return new ResponseEntity<>(createUser, HttpStatus.CREATED);

	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") int userId) {
		UserDto updateUser = this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updateUser);
	}

	//here i added role base logic
	//only admin role can delete user
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") int userId) {
		this.userService.deleteUser(userId);
		//return new ResponseEntity<>(Map.of("message", "user deleted successfully"), HttpStatus.OK);
		return new ResponseEntity<ApiResponse>(new ApiResponse("user deleted",true),HttpStatus.OK);
	}
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId")int userId){
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
}

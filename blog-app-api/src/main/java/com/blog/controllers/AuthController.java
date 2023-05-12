package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exceptions.ApiException;
import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.payloads.UserDto;
import com.blog.security.JwtTokenHelper;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest req) {

		this.authenticate(req.getUsername(), req.getPassword());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(req.getUsername());
		String token = this.jwtTokenHelper.generateToken(userDetails);

		JwtAuthResponse res = new JwtAuthResponse();
		res.setToken(token);

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	private void authenticate(String username, String password) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(
				username, password);
		try {
			this.authenticationManager.authenticate(usernamePasswordAuthToken);
		} catch (BadCredentialsException e) {
			System.out.println("user is disabled");
			throw new ApiException("invalid username or password");
		}

	}

	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
		UserDto registerUser = this.userService.registerNewUser(userDto);
		return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
	}
}

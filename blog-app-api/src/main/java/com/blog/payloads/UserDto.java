package com.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.blog.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;

	@Size(min = 4,message = "username must be min 4 character")
	@NotEmpty
	private String name;

	@NotEmpty(message = "email cann't empty")
	@Email(message = "email address not valid")
	private String email;

	@NotEmpty
	@Size(min = 3,max = 10,message = "password must be min 3 and max 10 chars")
	private String password;
	private String about;
	
	private Set<RoleDto> roles=new HashSet<>();


}

//@NotEmpty it check both notnull and notblank

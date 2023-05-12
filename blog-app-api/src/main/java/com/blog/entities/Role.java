package com.blog.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role {
	@Id
	
	private int id;
	
	
	private String name;
	

}

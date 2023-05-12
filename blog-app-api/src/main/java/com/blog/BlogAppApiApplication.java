package com.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.repositories.RoleRepo;

@SpringBootApplication
public class BlogAppApiApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("xyz"));
		try {
			Role role=new Role();
			role.setId(AppConstants.NORMAL_USER);
			role.setName("ROLE_NORMAL");
			
			Role role2=new Role();
			role2.setId(AppConstants.ADMIN_USER);
			role2.setName("ROLE_ADMIN");
			
			List<Role> rolesList = List.of(role,role2);
			List<Role> saveAll = roleRepo.saveAll(rolesList);
			saveAll.forEach(r->{
				System.out.println(r.getName());
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package com.blog.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER="Authorization";
	
	private ApiKey apiKeys() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}
	private List<SecurityContext> securityContext(){
		return Arrays.asList(SecurityContext.builder().securityReferences(securityReference()).build());
	}
	private List<SecurityReference> securityReference(){
		AuthorizationScope scopes=new AuthorizationScope("global","accessEveryThing");
		return Arrays.asList(new SecurityReference("JWT",new AuthorizationScope[] {scopes}));
	}
	@Bean
	public Docket api() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getInfo())
				.securityContexts(securityContext())
				.securitySchemes(Arrays.asList(apiKeys()))
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
				
				
				
	}

	private ApiInfo getInfo() {
		return new ApiInfo("Blogging Application : Backend","Developed by ThtsMe", "1.0","Terms and Service",new Contact("Mujahed]]","https://mitechs.in","md.thtsme@gamil.com"), "License of API","API License",Collections.EMPTY_LIST);
	}

}

package com.blog.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;
	
	@NotEmpty(message = "category title cannot be empty")
	@Size(min = 4,message = "min 4 char neeeded")
	private String categoryTitle;
	
	
	private String categoryDescription;
}

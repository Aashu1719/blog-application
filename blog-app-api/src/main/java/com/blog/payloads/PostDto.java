package com.blog.payloads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.blog.entities.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {

	private Integer postId;
	
	private String title;
	private String content;
	private String imageName;
	private Date addedDate;
	
	
	private CategoryDto category;
	private UserDto user;
	private List<CommentDto> comments=new ArrayList<CommentDto>();
}

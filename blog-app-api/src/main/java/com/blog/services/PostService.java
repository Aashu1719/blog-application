package com.blog.services;

import java.util.List;

import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

public interface PostService {
	
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
	
	PostDto updatePost(PostDto postDto,Integer postId);
	
	void deletePost(Integer postId);
	
	PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy);
	
	PostDto getPostById(Integer postId);
	
	//get all post by category 
	List<PostDto> getPostsByCategory(Integer categoryId);
	
	//get all posts by user
	List<PostDto> getPostsByUser(Integer userId);
	
	//search post
	List<PostDto> searchPosts(String keyword);
	

}

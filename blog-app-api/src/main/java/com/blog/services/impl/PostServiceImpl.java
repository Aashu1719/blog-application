package com.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "with user id", userId));
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category ", "category id", categoryId));

		Post post = modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post save = postRepo.save(post);

		return modelMapper.map(save, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "post id", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost = postRepo.save(post);
		return modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("posts", "post id", postId));
		postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy) {
		Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
//		Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		Page<Post> allpost = postRepo.findAll(p);
		List<Post> all = allpost.getContent();
		List<PostDto> dtoList = all.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postRes = new PostResponse();
		postRes.setContent(dtoList);
		postRes.setPageNumber(allpost.getNumber());
		postRes.setPageSize(allpost.getSize());
		postRes.setTotalElement(allpost.getTotalElements());
		postRes.setTotalPages(allpost.getTotalPages());
		postRes.setLastPage(allpost.isLast());
		return postRes;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("posts", "post id", postId));
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category cat = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category", "category id", categoryId));
		List<Post> posts = postRepo.findByCategory(cat);
		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", " userId", userId));
		List<Post> posts = postRepo.findByUser(user);
		List<PostDto> dtoList = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return dtoList;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = postRepo.findByTitleContaining("%"+keyword+"%");
		List<PostDto> list = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return list;
	}

}

package com.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.config.AppConstants;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.services.FileService;
import com.blog.services.PostService;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postServie;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createPost = postServie.createPost(postDto, userId, categoryId);

		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}

	// get by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable int userId) {
		List<PostDto> postsByUser = postServie.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(postsByUser, HttpStatus.OK);
	}

	// get by Category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable int categoryId) {
		List<PostDto> posts = postServie.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}

	// get post by postID
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getByPostId(@PathVariable int postId) {
		PostDto postById = postServie.getPostById(postId);
		return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
	}

	// get all posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy) {

		PostResponse allPost = postServie.getAllPost(pageNumber, pageSize, sortBy);
		return new ResponseEntity<PostResponse>(allPost, HttpStatus.FOUND);
	}

	// delete post by id
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePostById(@PathVariable int postId) {
		postServie.deletePost(postId);
		return ResponseEntity.ok(new ApiResponse("post deleted", true));
	}

	// Update Post	
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable int postId) {
		PostDto updatePost = postServie.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// search
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String keywords) {
		List<PostDto> searchPosts = this.postServie.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(searchPosts, HttpStatus.OK);
	}

	// post image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@PathVariable int postId, @RequestParam("image") MultipartFile image)
			throws IOException {

		PostDto byId = this.postServie.getPostById(postId);
		String filenName = this.fileService.uploadImage(path, image);
		byId.setImageName(filenName);
		PostDto updatePost = this.postServie.updatePost(byId, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}
	@GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName")String imageName,HttpServletResponse response)throws  IOException{
		InputStream resource=this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}

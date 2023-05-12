package com.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CommentDto;
import com.blog.repositories.CommentRepo;
import com.blog.repositories.PostRepo;
import com.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commetDto, Integer postId) {

		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post ", "Post id",postId));
		Comment comment = modelMapper.map(commetDto, Comment.class);
		comment.setPost(post);
		Comment save = this.commentRepo.save(comment);
		return modelMapper.map(save, CommentDto.class);
		
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment", "comment id", commentId));
		commentRepo.delete(comment);
		
	}
	

}

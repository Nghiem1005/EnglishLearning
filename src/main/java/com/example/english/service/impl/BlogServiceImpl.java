package com.example.english.service.impl;

import com.example.english.dto.request.BlogRequestDTO;
import com.example.english.dto.response.BlogResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Blog;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.BlogMapper;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.BlogRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.BlogService;
import com.example.english.service.StorageService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {
  @Autowired private StorageService storageService;
  @Autowired private UserRepository userRepository;
  @Autowired private BlogRepository blogRepository;

  @Override
  public ResponseEntity<?> createBlog(Long userId, BlogRequestDTO blogRequestDTO) throws IOException {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Blog blog = BlogMapper.INSTANCE.blogRequestDTOToBlog(blogRequestDTO);

    if (blogRequestDTO.getImage() != null) {
      blog.setImage(storageService.uploadFile(blogRequestDTO.getImage()));
    }

    blog.setUser(user);

    BlogResponseDTO blogResponseDTO = BlogMapper.INSTANCE.blogToBlogResponseDTO(blogRepository.save(blog));

    blogResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(user));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create blog success", blogResponseDTO));
  }

  @Override
  public ResponseEntity<?> getBlogById(Long blogId) {
    Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Could not find blog with ID = " + blogId));

    BlogResponseDTO blogResponseDTO = BlogMapper.INSTANCE.blogToBlogResponseDTO(blogRepository.save(blog));

    blogResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(blog.getUser()));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get blog", blogResponseDTO));
  }
}

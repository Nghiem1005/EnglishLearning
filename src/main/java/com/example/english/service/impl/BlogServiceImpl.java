package com.example.english.service.impl;

import com.example.english.dto.request.BlogRequestDTO;
import com.example.english.dto.response.BlogResponseDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Bill;
import com.example.english.entities.Blog;
import com.example.english.entities.Comment;
import com.example.english.entities.Course;
import com.example.english.entities.Feedback;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.BlogMapper;
import com.example.english.mapper.FeedbackMapper;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.BlogRepository;
import com.example.english.repository.CommentRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.BlogService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {
  @Autowired private StorageService storageService;
  @Autowired private UserRepository userRepository;
  @Autowired private BlogRepository blogRepository;
  @Autowired private CommentRepository commentRepository;

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
  public ResponseEntity<?> updateBlog(Long blogId, BlogRequestDTO blogRequestDTO)
      throws IOException {
    Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Could not find blog with ID = " + blogId));

    if (blogRequestDTO.getTitle() != null) {
      blog.setTitle(blogRequestDTO.getTitle());
    }

    if (blogRequestDTO.getContent() != null) {
      blog.setContent(blogRequestDTO.getContent());
    }

    if (blogRequestDTO.getImage() != null) {
      blog.setImage(storageService.uploadFile(blogRequestDTO.getImage()));
    }

    BlogResponseDTO blogResponseDTO = BlogMapper.INSTANCE.blogToBlogResponseDTO(blogRepository.save(blog));

    blogResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(blog.getUser()));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create blog success", blogResponseDTO));
  }

  @Override
  public ResponseEntity<?> getBlogById(Long blogId) {
    Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Could not find blog with ID = " + blogId));

    BlogResponseDTO blogResponseDTO = BlogMapper.INSTANCE.blogToBlogResponseDTO(blog);

    //Get total comment
    List<Comment> commentList = commentRepository.findCommentsByBlog(blog);
    blogResponseDTO.setTotalComment(commentList.size());

    blogResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(blog.getUser()));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get blog", blogResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllBlog(Pageable pageable) {
    Page<Blog> getBlogList = blogRepository.findAll(pageable);
    List<Blog> blogList = getBlogList.getContent();
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List blog.", toListBlogResponseDTO(blogList),
        getBlogList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getBlogByUser(Long userId, Pageable pageable) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Page<Blog> getBlogList = blogRepository.findBlogsByUser(user, pageable);
    List<Blog> blogList = getBlogList.getContent();
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List blog.", toListBlogResponseDTO(blogList),
        getBlogList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> deleteBlog(Long blogId) {
    Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Could not find blog with ID = " + blogId));

    //Delete file image
    storageService.deleteFile(blog.getImage());

    blogRepository.delete(blog);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete blog success!"));
  }

  private List<BlogResponseDTO> toListBlogResponseDTO(List<Blog> blogList){
    List<BlogResponseDTO> blogResponseDTOS = new ArrayList<>();
    for (Blog blog : blogList) {
      BlogResponseDTO blogResponseDTO = BlogMapper.INSTANCE.blogToBlogResponseDTO(blog);

      //Get user is creater of blog
      blogResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(blog.getUser()));

      //Get total blog
      List<Comment> commentList = commentRepository.findCommentsByBlog(blog);
      blogResponseDTO.setTotalComment(commentList.size());

      blogResponseDTOS.add(blogResponseDTO);
    }

    return blogResponseDTOS;
  }
}

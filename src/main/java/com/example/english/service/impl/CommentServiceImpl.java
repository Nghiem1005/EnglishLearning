package com.example.english.service.impl;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Blog;
import com.example.english.entities.Comment;
import com.example.english.entities.Course;
import com.example.english.entities.Feedback;
import com.example.english.entities.StudentCourse;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.CommentMapper;
import com.example.english.mapper.FeedbackMapper;
import com.example.english.repository.BlogRepository;
import com.example.english.repository.CommentRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.CommentService;
import com.example.english.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
  @Autowired private CommentRepository commentRepository;
  @Autowired private BlogRepository blogRepository;
  @Autowired private UserRepository userRepository;
  @Override
  public ResponseEntity<?> getAllComment(Pageable pageable) {
    Page<Comment> commentPage = commentRepository.findAll(pageable);
    List<Comment> commentList = commentPage.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List comment.", toListCommentResponseDTO(commentList),
        commentPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getAllCommentByBlog(Pageable pageable, Long blogId) {
    Blog blog = blogRepository.findById(blogId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find blog with ID = " + blogId));
    List<Comment> comments = commentRepository.findCommentsByBlog(blog);
    Page<Comment> commentPage = commentRepository.findCommentsByBlog(pageable, blog);
    List<Comment> commentList = commentPage.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List comment.", toListCommentResponseDTO(commentList),
        commentPage.getTotalPages(), comments.size()));
  }

  @Override
  public ResponseEntity<?> createComment(DiscussRequestDTO commentRequestDTO) throws IOException {
    Comment comment = CommentMapper.INSTANCE.commentRequestDTOToComment(commentRequestDTO);

    User user = userRepository.findById(commentRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + commentRequestDTO.getStudentId()));
    Blog blog = blogRepository.findById(commentRequestDTO.getSubjectId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find blog with ID = " + commentRequestDTO.getSubjectId()));

    comment.setBlog(blog);
    comment.setUser(user);

    if (commentRequestDTO.getImages() != null){
      List<String> nameFiles = Utils.storeFile(commentRequestDTO.getImages());
      comment.setImages(nameFiles);
    }
    DiscussResponseDTO commentMainResponseDTO = new DiscussResponseDTO();
    if (commentRequestDTO.getMainDiscuss() != null) {
      Comment commentMain = commentRepository.findById(commentRequestDTO.getMainDiscuss())
          .orElseThrow(() -> new ResourceNotFoundException("Could not find comment with ID = " + commentRequestDTO.getMainDiscuss()));
      comment.setMainComment(commentMain);
      commentMainResponseDTO = CommentMapper.INSTANCE.commentToCommentResponseDTO(commentMain);
    }

    Comment commentSaved = commentRepository.save(comment);
    DiscussResponseDTO commentResponseDTO = CommentMapper.INSTANCE.commentToCommentResponseDTO(commentSaved);
    commentResponseDTO.setMainDiscuss(commentMainResponseDTO);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create comment successfully!", commentResponseDTO));
  }

  @Override
  public ResponseEntity<?> updateComment(String content, Long id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find comment with ID = " + id));
    comment.setId(id);
    comment.setContent(content);
    Comment commentSaved = commentRepository.save(comment);
    DiscussResponseDTO commentResponseDTO = CommentMapper.INSTANCE.commentToCommentResponseDTO(commentSaved);
    if (comment.getMainComment() != null) {
      commentResponseDTO.setMainDiscuss(CommentMapper.INSTANCE.commentToCommentResponseDTO(comment.getMainComment()));
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update comment successfully!", commentResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteComment(Long id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find comment with ID = " + id));

    commentRepository.delete(comment);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete comment successfully!"));
  }

  @Override
  public ResponseEntity<?> getCommentById(Long id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find comment with ID = " + id));
    DiscussResponseDTO commentResponseDTO = CommentMapper.INSTANCE.commentToCommentResponseDTO(comment);

    if (comment.getMainComment() != null) {
      commentResponseDTO.setMainDiscuss(CommentMapper.INSTANCE.commentToCommentResponseDTO(comment.getMainComment()));
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get comment.", commentResponseDTO));
  }

  @Override
  public ResponseEntity<?> getCommentByCommentMain(Pageable pageable, Long commentMain) {
    Comment comment = commentRepository.findById(commentMain)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find comment with ID = " + commentMain));

    List<Comment> comments = commentRepository.findCommentsByMainComment(comment);
    Page<Comment> commentPage = commentRepository.findCommentsByMainComment(pageable, comment);
    List<Comment> commentList = commentPage.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List comment.", toListCommentResponseDTO(commentList),
        commentPage.getTotalPages(), comments.size()));
  }

  private List<DiscussResponseDTO> toListCommentResponseDTO(List<Comment> commentList){
    //Convert feedback to feedback response dto
    List<DiscussResponseDTO> commentResponseDTOList = new ArrayList<>();
    for (Comment comment : commentList) {
      DiscussResponseDTO commentResponseDTO = CommentMapper.INSTANCE.commentToCommentResponseDTO(comment);
      if (comment.getMainComment() != null) {
        commentResponseDTO.setMainDiscuss(CommentMapper.INSTANCE.commentToCommentResponseDTO(comment.getMainComment()));
      }
      commentResponseDTOList.add(commentResponseDTO);
    }

    return commentResponseDTOList;
  }
}

package com.example.english.service;

import com.example.english.dto.request.DiscussRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CommentService {
  ResponseEntity<?> getAllComment(Pageable pageable);

  ResponseEntity<?> getAllCommentByBlog(Pageable pageable, Long blogId);

  ResponseEntity<?> createComment(DiscussRequestDTO commentRequestDTO) throws IOException;

  ResponseEntity<?> updateComment(String content, Long id);

  ResponseEntity<?> deleteComment(Long id);

  ResponseEntity<?> getCommentById(Long id);

  ResponseEntity<?> getCommentByCommentMain(Pageable pageable, Long commentMain);
}

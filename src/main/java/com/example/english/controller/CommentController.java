package com.example.english.controller;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.service.CommentService;
import com.example.english.utils.Utils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/comment")
public class CommentController {
  @Autowired private CommentService commentService;
  @GetMapping(value = "")
  public ResponseEntity<?> getAllComment(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return commentService.getAllComment(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<?> createComment(@ModelAttribute DiscussRequestDTO commentRequestDTO)
      throws IOException {
    return commentService.createComment(commentRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateComment(@RequestParam(name = "content") String content,
      @RequestParam(name = "id") Long id) {
    return commentService.updateComment(content, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteComment(@RequestParam(name = "id") Long id) {
    return commentService.deleteComment(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getCommentById(@PathVariable(name = "id") Long id) {
    return commentService.getCommentById(id);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getAllCommentByCourse(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "blogId") Long blogId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return commentService.getAllCommentByBlog(pageable, blogId);
  }

  @GetMapping(value = "/CommentMain")
  public ResponseEntity<?> getAllCommentByCommentMain(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "commentMainId") Long commentMainId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return commentService.getCommentByCommentMain(pageable, commentMainId);
  }
}

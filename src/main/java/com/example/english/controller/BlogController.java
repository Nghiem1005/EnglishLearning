package com.example.english.controller;

import com.example.english.dto.request.BlogRequestDTO;
import com.example.english.service.BlogService;
import com.example.english.utils.Utils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/blog")
public class BlogController {
  @Autowired private BlogService blogService;
  @PostMapping(value = "")
  public ResponseEntity<?> createBlog(@RequestParam(name = "userId") Long userId,@ModelAttribute BlogRequestDTO blogRequestDTO)
      throws IOException {
    return blogService.createBlog(userId, blogRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateBlog(@RequestParam(name = "blogId") Long blogId,@ModelAttribute BlogRequestDTO blogRequestDTO)
      throws IOException {
    return blogService.updateBlog(blogId, blogRequestDTO);
  }

  @GetMapping(value = "{blogId}")
  public ResponseEntity<?> getBlogById(@PathVariable(name = "blogId") Long blogId) {
    return blogService.getBlogById(blogId);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllBlog(@RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
    return blogService.getAllBlog(pageable);
  }

  @GetMapping(value = "/user")
  public ResponseEntity<?> getBlogByUser(
      @RequestParam(name = "userId") Long userId,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
    return blogService.getBlogByUser(userId, pageable);
  }
}

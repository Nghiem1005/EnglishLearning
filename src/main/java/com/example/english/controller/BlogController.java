package com.example.english.controller;

import com.example.english.dto.request.BlogRequestDTO;
import com.example.english.service.BlogService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/blog")
public class BlogController {
  @Autowired private BlogService blogService;
  @PostMapping(value = "")
  public ResponseEntity<?> createStudentCourse(@RequestParam(name = "userId") Long userId,@ModelAttribute BlogRequestDTO blogRequestDTO)
      throws IOException {
    return blogService.createBlog(userId, blogRequestDTO);
  }

  @GetMapping(value = "{blogId}")
  public ResponseEntity<?> getBlogById(@PathVariable(name = "blogId") Long blogId) {
    return blogService.getBlogById(blogId);
  }
}

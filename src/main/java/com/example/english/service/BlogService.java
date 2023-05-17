package com.example.english.service;

import com.example.english.dto.request.BlogRequestDTO;
import java.io.IOException;
import org.springframework.http.ResponseEntity;

public interface BlogService {
 ResponseEntity<?> createBlog(Long userId, BlogRequestDTO blogRequestDTO) throws IOException;
 ResponseEntity<?> getBlogById(Long blogId);
}

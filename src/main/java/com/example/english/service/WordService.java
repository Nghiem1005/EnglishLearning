package com.example.english.service;

import com.example.english.dto.request.WordRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface WordService {
  ResponseEntity<?> addWord(Long listWordId, List<WordRequestDTO> wordRequestDTOS)
      throws IOException;
}

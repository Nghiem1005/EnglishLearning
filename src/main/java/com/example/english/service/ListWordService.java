package com.example.english.service;

import com.example.english.dto.request.ListWordRequestDTO;
import java.io.IOException;
import org.springframework.http.ResponseEntity;

public interface ListWordService {
  ResponseEntity<?> createListWord(Long userId, ListWordRequestDTO listWordRequestDTO) throws IOException;
  ResponseEntity<?> getListWordById(Long listWordId);
  ResponseEntity<?> addWordToListWord(Long listWordId, ListWordRequestDTO listWordRequestDTO) throws IOException;
}

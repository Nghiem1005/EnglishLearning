package com.example.english.service;

import com.example.english.dto.request.CourseRequestDTO;
import com.example.english.dto.request.ListWordRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ListWordService {
  ResponseEntity<?> createListWord(Long userId, ListWordRequestDTO listWordRequestDTO) throws IOException;
  ResponseEntity<?> updateListWord(Long listWordId, ListWordRequestDTO listWordRequestDTO);
  ResponseEntity<?> getListWordById(Pageable pageable, Long listWordId);
  ResponseEntity<?> getListWordByUser(Pageable pageable, Long userId);
  ResponseEntity<?> deleteListWord(Long listWordId);
}

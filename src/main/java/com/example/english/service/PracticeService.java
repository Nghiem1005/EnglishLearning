package com.example.english.service;

import com.example.english.dto.request.PracticeRequestDTO;
import org.springframework.http.ResponseEntity;

public interface PracticeService {
  ResponseEntity<?> createPractice(Long userId, PracticeRequestDTO practiceRequestDTO);
  ResponseEntity<?> updatePractice(Long practiceId, PracticeRequestDTO practiceRequestDTO);
}

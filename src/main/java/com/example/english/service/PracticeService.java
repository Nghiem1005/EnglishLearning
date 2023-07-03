package com.example.english.service;

import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PracticeService {
  ResponseEntity<?> createPractice(Long userId, PracticeRequestDTO practiceRequestDTO);
  ResponseEntity<?> updatePractice(Long practiceId, PracticeRequestDTO practiceRequestDTO);
  ResponseEntity<?> getPracticeResultByUser(Long userId, Pageable pageable);
  ResponseEntity<?> getPracticeResultByPractice(Long practiceId);
}

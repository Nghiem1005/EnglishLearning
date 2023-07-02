package com.example.english.service;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import org.springframework.http.ResponseEntity;

public interface PartService {
  ResponseEntity<?> createPart(Long examId, PartRequestDTO partRequestDTO);
  ResponseEntity<?> updatePart(Long partId, PracticeRequestDTO practiceRequestDTO);
  ResponseEntity<?> deletePart(Long partId);
  ResponseEntity<?> getPartById(Long partId);
}

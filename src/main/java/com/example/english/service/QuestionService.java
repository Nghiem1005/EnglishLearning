package com.example.english.service;

import com.example.english.dto.request.QuestionRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
  ResponseEntity<?> addQuestion(Long partId, List<QuestionRequestDTO> questionRequestDTOS)
      throws IOException;

  ResponseEntity<?> updateQuestion(Long id, QuestionRequestDTO questionRequestDTO)
      throws IOException;

  ResponseEntity<?> deleteQuestion(Long id);
}

package com.example.english.service;

import com.example.english.dto.request.AnswerRequestDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AnswerService {
  ResponseEntity<?> addAnswer(Long questionId, List<AnswerRequestDTO> answerRequestDTOS);
  ResponseEntity<?> updateAnswer(Long id, AnswerRequestDTO answerRequestDTO);

  ResponseEntity<?> deleteAnswer(Long id);
}

package com.example.english.service;

import com.example.english.dto.request.AnswerRequestDTO;
import com.example.english.dto.response.AnswerResponseDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AnswerService {
  ResponseEntity<?> addAnswer(Long questionId, AnswerRequestDTO answerRequestDTO);
  AnswerResponseDTO createAnswer(Long questionId, AnswerRequestDTO answerRequestDTO);
  ResponseEntity<?> updateAnswer(Long id, AnswerRequestDTO answerRequestDTO);

  ResponseEntity<?> deleteAnswer(Long id);
}

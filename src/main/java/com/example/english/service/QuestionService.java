package com.example.english.service;

import com.example.english.dto.request.QuestionRequestDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
  ResponseEntity<?> addQuestion(Long questionPhraseId, QuestionRequestDTO questionRequestDTO)
      throws IOException;

  QuestionResponseDTO createQuestion(Long questionPhraseId, QuestionRequestDTO questionRequestDTO)
      throws IOException;

  ResponseEntity<?> updateQuestion(Long id, QuestionRequestDTO questionRequestDTO)
      throws IOException;

  ResponseEntity<?> deleteQuestion(Long id);
  ResponseEntity<?> getQuestionById(Long id);
}

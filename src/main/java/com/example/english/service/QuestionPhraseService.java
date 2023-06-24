package com.example.english.service;

import com.example.english.dto.request.QuestionPhraseRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface QuestionPhraseService {
  ResponseEntity<?> createQuestionPhrase(Long partId, List<QuestionPhraseRequestDTO> questionPhraseRequestDTOS)
      throws IOException;
}

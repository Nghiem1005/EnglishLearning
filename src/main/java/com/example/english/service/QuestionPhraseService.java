package com.example.english.service;

import com.example.english.dto.request.QuestionPhraseRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionPhraseService {
  ResponseEntity<?> createQuestionPhrase(Long partId, QuestionPhraseRequestDTO questionPhraseRequestDTO, MultipartFile[] documents)
      throws IOException;
}

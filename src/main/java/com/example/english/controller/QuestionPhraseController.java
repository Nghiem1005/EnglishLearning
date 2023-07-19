package com.example.english.controller;

import com.example.english.dto.request.QuestionPhraseRequestDTO;
import com.example.english.dto.request.QuestionRequestDTO;
import com.example.english.service.QuestionPhraseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/question-phrase")
public class QuestionPhraseController {
  @Autowired private QuestionPhraseService questionPhraseService;

  @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @RequestBody(content = @Content(encoding = @Encoding(name = "questionPhraseRequestDTO", contentType = "application/json")))
  public ResponseEntity<?> addQuestionPhrase(@RequestPart QuestionPhraseRequestDTO questionPhraseRequestDTO, @RequestParam(name = "partId") Long partId,
      @RequestPart(required = false) MultipartFile[] documents) throws IOException {
    return questionPhraseService.createQuestionPhrase(partId, questionPhraseRequestDTO, documents);
  }
}

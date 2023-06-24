package com.example.english.controller;

import com.example.english.dto.request.QuestionRequestDTO;
import com.example.english.service.QuestionService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionController {
  @Autowired private QuestionService questionService;
  @PostMapping(value = "")
  public ResponseEntity<?> addQuestion(@RequestBody QuestionRequestDTO questionRequestDTO, @RequestParam(name = "questionPhraseId") Long questionPhraseId) throws IOException {
    return questionService.addQuestion(questionPhraseId, questionRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateQuestion(@RequestBody QuestionRequestDTO questionRequestDTO,
      @RequestParam(name = "id") Long id) throws IOException {
    return questionService.updateQuestion(id, questionRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteQuestion(@RequestParam(name = "id") Long id) {
    return questionService.deleteQuestion(id);
  }
}

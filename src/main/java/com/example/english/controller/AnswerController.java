package com.example.english.controller;

import com.example.english.dto.request.AnswerRequestDTO;
import com.example.english.service.AnswerService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/answer")
public class AnswerController {
  @Autowired
  private AnswerService answerService;
  @PostMapping(value = "")
  public ResponseEntity<?> addAnswer(@ModelAttribute List<AnswerRequestDTO> answerRequestDTOS, @RequestParam(name = "questionId") Long questionId) throws IOException {
    return answerService.addAnswer(questionId, answerRequestDTOS);
  }
}

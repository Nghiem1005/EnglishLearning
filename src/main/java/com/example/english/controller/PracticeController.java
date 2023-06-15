package com.example.english.controller;

import com.example.english.dto.request.ContestRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.service.ContestService;
import com.example.english.service.PracticeService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/practice")
public class PracticeController {
  @Autowired
  private PracticeService practiceService;

  @PostMapping(value = "")
  public ResponseEntity<?> createPractice(@RequestParam(name = "userId") Long userId,@ModelAttribute PracticeRequestDTO practiceRequestDTO) {
    return practiceService.createPractice(userId, practiceRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updatePractice(@RequestParam(name = "practiceId") Long practiceId, @RequestBody PracticeRequestDTO practiceRequestDTO) {
    return practiceService.updatePractice(practiceId, practiceRequestDTO);
  }
}

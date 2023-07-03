package com.example.english.controller;

import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.service.PracticeService;
import com.example.english.utils.Utils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<?> createPractice(@RequestParam(name = "userId") Long userId,@RequestBody PracticeRequestDTO practiceRequestDTO) {
    return practiceService.createPractice(userId, practiceRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updatePractice(@RequestParam(name = "practiceId") Long practiceId, @RequestBody PracticeRequestDTO practiceRequestDTO) {
    return practiceService.updatePractice(practiceId, practiceRequestDTO);
  }

  @GetMapping(value = "/result/user/{userId}")
  public ResponseEntity<?> getPracticeResultByUser(@PathVariable(name = "userId") Long userId,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return practiceService.getPracticeResultByUser(userId, pageable);
  }

  @GetMapping(value = "/result/{practiceId}")
  public ResponseEntity<?> getPracticeResultByPractice(@PathVariable(name = "practiceId") Long practiceId) {
    return practiceService.getPracticeResultByPractice(practiceId);
  }
}

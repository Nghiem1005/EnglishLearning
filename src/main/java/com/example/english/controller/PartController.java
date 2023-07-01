package com.example.english.controller;

import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.request.PartRequestDTO;
import com.example.english.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/part")
public class PartController {
  @Autowired private PartService partService;

  @PostMapping(value = "")
  public ResponseEntity<?> createPart(@RequestBody PartRequestDTO partRequestDTO, @RequestParam(name = "examId") Long examId) {
    return partService.createPart(examId, partRequestDTO);
  }
}

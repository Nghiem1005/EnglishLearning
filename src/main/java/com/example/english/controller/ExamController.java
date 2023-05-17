package com.example.english.controller;

import com.example.english.dto.request.ExerciseRequestDTO;
import com.example.english.service.ExamService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/exam")
public class ExamController {
  @Autowired private ExamService examService;

  @PostMapping(value = "/student")
  public ResponseEntity<?> createExam(@RequestParam(name = "lessonId") Long lessonId, @ModelAttribute
  List<ExerciseRequestDTO> examRequestDTOS) throws IOException {
    return examService.createExam(lessonId, examRequestDTOS);
  }

  @DeleteMapping (value = "/student")
  public ResponseEntity<?> deleteExam(@RequestParam(name = "lessonId") Long lessonId, @RequestParam(name = "partId") Long partId) throws IOException {
    return examService.deleteExam(lessonId, partId);
  }
}

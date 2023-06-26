package com.example.english.controller;

import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.request.PartRequestDTO;
import com.example.english.service.ExamService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/exam")
public class ExamController {
  @Autowired private ExamService examService;

  @PostMapping(value = "")
  public ResponseEntity<?> createExam(@ModelAttribute ExamRequestDTO examRequestDTO) {
    return examService.createExam(examRequestDTO);
  }

  @PostMapping(value = "/lesson")
  public ResponseEntity<?> createExamLesson(@RequestParam(name = "lessonId") Long lessonId ,@ModelAttribute ExamRequestDTO examRequestDTO) {
    return examService.createExamLesson(lessonId, examRequestDTO);
  }

  @DeleteMapping (value = "")
  public ResponseEntity<?> deleteExam(@RequestParam(name = "examId") Long examId) throws IOException {
    return examService.deleteExam(examId);
  }
}

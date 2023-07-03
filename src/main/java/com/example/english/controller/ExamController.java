package com.example.english.controller;

import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.request.PartRequestDTO;
import com.example.english.service.ExamService;
import com.example.english.utils.Utils;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<?> createExam(@RequestBody ExamRequestDTO examRequestDTO) {
    return examService.createExam(examRequestDTO);
  }

  @PostMapping(value = "/lesson")
  public ResponseEntity<?> createExamLesson(@RequestParam(name = "lessonId") Long lessonId ,@RequestBody ExamRequestDTO examRequestDTO) {
    return examService.createExamLesson(lessonId, examRequestDTO);
  }

  @GetMapping
  public ResponseEntity<?> getListExam(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
  @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return examService.getAllExam(pageable);
  }

  @GetMapping(value = "/lesson")
  public ResponseEntity<?> getExamByLesson(@RequestParam(name = "lessonId") Long lessonId) {
    return examService.getExamByLesson(lessonId);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getExamById(@PathVariable(name = "id") Long id) {
    return examService.getExamById(id);
  }

  @DeleteMapping (value = "")
  public ResponseEntity<?> deleteExam(@RequestParam(name = "examId") Long examId) throws IOException {
    return examService.deleteExam(examId);
  }
}

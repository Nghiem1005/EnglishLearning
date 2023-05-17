package com.example.english.controller;

import com.example.english.dto.request.LessonRequestDTO;
import com.example.english.service.LessonService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/lesson")
public class LessonController {
  @Autowired private LessonService lessonService;

  @PostMapping(value = "")
  public ResponseEntity<?> createLesson(@RequestParam(name = "courseId") Long courseId,
      @ModelAttribute LessonRequestDTO lessonRequestDTO) throws IOException {
    return lessonService.createLesson(courseId, lessonRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateLesson(@ModelAttribute LessonRequestDTO lessonRequestDTO,
      @RequestParam(name = "id") Long id) throws IOException {
    return lessonService.updateLesson(id, lessonRequestDTO);
  }


  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getLessonById(@PathVariable(name = "id") Long id) {
    return lessonService.getLessonById(id);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getAllLessonByCourse(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "courseId") Long courseId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return lessonService.getAllLessonByCourse(pageable, courseId);
  }
}

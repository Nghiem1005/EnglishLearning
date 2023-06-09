package com.example.english.controller;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.service.FeedbackService;
import com.example.english.utils.Utils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/feedback")
public class FeedbackController {
  @Autowired
  private FeedbackService feedbackService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllFeedback(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedback(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<?> createFeedback(@ModelAttribute DiscussRequestDTO feedbackRequestDTO)
      throws IOException {
    return feedbackService.createFeedback(feedbackRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateFeedback(@ModelAttribute DiscussRequestDTO feedbackRequestDTO,
      @RequestParam(name = "id") Long id) throws IOException {
    return feedbackService.updateFeedback(feedbackRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteFeedback(@RequestParam(name = "id") Long id) {
    return feedbackService.deleteFeedback(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getFeedbackById(@PathVariable(name = "id") Long id) {
    return feedbackService.getFeedbackById(id);
  }

  @GetMapping(value = "/course")
  public ResponseEntity<?> getAllFeedbackByCourse(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "courseId") Long courseId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedbackByCourse(pageable, courseId);
  }

  @GetMapping(value = "/course/pending")
  public ResponseEntity<?> getAllFeedbackByCourseAndPending(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "courseId") Long courseId, @RequestParam(name = "pending", defaultValue = "false") boolean pending) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getAllFeedbackByCourseAndPending(pageable, courseId, pending);
  }

  @GetMapping(value = "/feedbackMain")
  public ResponseEntity<?> getAllFeedbackByFeedbackMain(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "feedbackMainId") Long feedbackMainId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return feedbackService.getFeedbackByFeedbackMain(pageable, feedbackMainId);
  }
}

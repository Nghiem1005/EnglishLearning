package com.example.english.controller;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.service.InquiryService;
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
@RequestMapping(value = "/api/v1/inquiry")
public class InquiryController {
  @Autowired
  private InquiryService inquiryService;

  @PostMapping(value = "")
  public ResponseEntity<?> createInquiry(@ModelAttribute DiscussRequestDTO inquiryRequestDTO)
      throws IOException {
    return inquiryService.createInquiry(inquiryRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateInquiry(@ModelAttribute DiscussRequestDTO inquiryRequestDTO,
      @RequestParam(name = "id") Long id) throws IOException {
    return inquiryService.updateInquiry(inquiryRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteInquiry(@RequestParam(name = "id") Long id) {
    return inquiryService.deleteInquiry(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getInquiryById(@PathVariable(name = "id") Long id) {
    return inquiryService.getInquiryById(id);
  }

  @GetMapping(value = "/lesson")
  public ResponseEntity<?> getAllInquiryByCourse(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "lessonId") Long lessonId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return inquiryService.getAllInquiryByLesson(pageable, lessonId);
  }

  @GetMapping(value = "/inquiryMain")
  public ResponseEntity<?> getAllFeedbackByFeedbackMain(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "inquiryMainId") Long inquiryMainId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return inquiryService.getInquiryByInquiryMain(pageable, inquiryMainId);
  }
}

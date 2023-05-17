package com.example.english.service;

import com.example.english.dto.request.DiscussRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface InquiryService {
  ResponseEntity<?> getAllFeedback(Pageable pageable);

  ResponseEntity<?> getAllInquiryByLesson(Pageable pageable, Long lessonId);

  ResponseEntity<?> createInquiry(DiscussRequestDTO inquiryRequestDTO) throws IOException;

  ResponseEntity<?> updateInquiry(String content, Long id);

  ResponseEntity<?> deleteInquiry(Long id);

  ResponseEntity<?> getInquiryById(Long id);

  ResponseEntity<?> getInquiryByInquiryMain(Pageable pageable, Long inquiryMain);
}

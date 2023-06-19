package com.example.english.service;

import com.example.english.dto.request.DiscussRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface FeedbackService {
    ResponseEntity<?> getAllFeedback(Pageable pageable);

    ResponseEntity<?> getAllFeedbackByCourse(Pageable pageable, Long courseId);

    ResponseEntity<?> getAllFeedbackByCourseAndPending(Pageable pageable, Long courseId, boolean pending);

    ResponseEntity<?> createFeedback(DiscussRequestDTO feedbackRequestDTO) throws IOException;

    ResponseEntity<?> updateFeedback(String content, Long id);

    ResponseEntity<?> deleteFeedback(Long id);

    ResponseEntity<?> getFeedbackById(Long id);

    ResponseEntity<?> getFeedbackByFeedbackMain(Pageable pageable, Long feedbackMain);
}
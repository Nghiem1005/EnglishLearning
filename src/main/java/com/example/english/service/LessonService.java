package com.example.english.service;

import com.example.english.dto.request.LessonRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface LessonService {
  ResponseEntity<?> getAllLessonByCourse(Pageable pageable, Long courseId);

  ResponseEntity<?> createLesson(Long courseId, LessonRequestDTO lessonRequestDTO)
      throws IOException;

  ResponseEntity<?> updateLesson(Long id, LessonRequestDTO lessonRequestDTO) throws IOException;

  ResponseEntity<?> deleteLesson(Long id);

  ResponseEntity<?> getLessonById(Long id);
}

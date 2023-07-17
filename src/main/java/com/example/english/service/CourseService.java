package com.example.english.service;

import com.example.english.dto.request.CourseRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CourseService {
  ResponseEntity<?> saveCourse(Long teacherId, CourseRequestDTO courseRequestDTO)
      throws IOException;
  ResponseEntity<?> updateCourse(Long courseId, CourseRequestDTO courseRequestDTO)
      throws IOException;
  ResponseEntity<?> deleteCourse(Long courseId);
  ResponseEntity<?> getAllCourse(Long userId, Pageable pageable);
  ResponseEntity<?> getCourseById(Long id, Long userId);
  ResponseEntity<?> getCourseByTeacher(Pageable pageable, Long teacherId);
  ResponseEntity<?> filterCourse(Long userId, Pageable pageable, String search);
  ResponseEntity<?> getCourseByCategory(Pageable pageable, String type, int point);
  ResponseEntity<?> getCourseByNotTeacher(Pageable pageable);
  ResponseEntity<?> getCourseByType(Pageable pageable, String type);
}

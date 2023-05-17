package com.example.english.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface StudentCourseService {
  ResponseEntity<?> createStudentCourse(Long studentId, Long courseId);
  ResponseEntity<?> deleteStudentCourse(Long studentId, Long courseId);
  ResponseEntity<?> getCourseByStudent(Pageable pageable, Long studentId);
  ResponseEntity<?> getStudentByCourse(Pageable pageable, Long courseId);
  ResponseEntity<?> updateProgress(Long studentId, Long courseId, int progress);
}

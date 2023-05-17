package com.example.english.service;

import org.springframework.http.ResponseEntity;

public interface LikeCourseService {
  ResponseEntity<?> createLikeCourse(Long studentId, Long courseId);
  ResponseEntity<?> deleteLikeCourse(Long studentId, Long courseId);
}

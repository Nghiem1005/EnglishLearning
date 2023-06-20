package com.example.english.service;

import org.springframework.http.ResponseEntity;

public interface StatisticService {
  ResponseEntity<?> statisticInfoCourse(Long courseId);
  ResponseEntity<?> statisticBestSeller();
  ResponseEntity<?> generalStatistics();
  ResponseEntity<?> statisticsByDay();
}

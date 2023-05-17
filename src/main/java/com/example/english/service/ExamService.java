package com.example.english.service;

import com.example.english.dto.request.ExerciseRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ExamService {
 ResponseEntity<?> createExam(Long lessonId, List<ExerciseRequestDTO> examRequestDTOS) throws IOException;
 ResponseEntity<?> deleteExam(Long lessonId, Long partId);
}

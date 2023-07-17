package com.example.english.service;

import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.request.PartRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ExamService {
 ResponseEntity<?> createExam(ExamRequestDTO examRequestDTO);
 ResponseEntity<?> createExamLesson(Long lessonId, ExamRequestDTO examRequestDTO);
 ResponseEntity<?> updateExam(Long examId, ExamRequestDTO examRequestDTO);
 ResponseEntity<?> deleteExam(Long examId);
 ResponseEntity<?> getExamByLesson(Long lessonId);
 ResponseEntity<?> getExamById(Long id);
 ResponseEntity<?> getAllExam(Pageable pageable);
}

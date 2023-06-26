package com.example.english.service;

import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.request.PartRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ExamService {
 ResponseEntity<?> createExam(ExamRequestDTO examRequestDTO);
 ResponseEntity<?> createExamLesson(Long lessonId, ExamRequestDTO examRequestDTO);
 ResponseEntity<?> deleteExam(Long examId);
}

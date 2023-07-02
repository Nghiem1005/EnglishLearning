package com.example.english.repository;

import com.example.english.entities.Exam;
import com.example.english.entities.Lesson;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
  Page<Exam> findExamsByLessonIsNull(Pageable pageable);
  Optional<Exam> findExamByLesson(Lesson lesson);
}

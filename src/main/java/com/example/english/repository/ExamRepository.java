package com.example.english.repository;

import com.example.english.entities.Exam;
import com.example.english.entities.Lesson;
import com.example.english.entities.Part;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
  List<Exam> findExamsByLesson(Lesson lesson);

  Optional<Exam> findExamsByLessonAndPart(Lesson lesson, Part part);
}

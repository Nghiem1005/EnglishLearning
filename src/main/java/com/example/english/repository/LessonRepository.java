package com.example.english.repository;

import com.example.english.entities.Course;
import com.example.english.entities.Lesson;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
  Optional<Lesson> findLessonByName(String name);
  Page<Lesson> findLessonsByCourse(Course course, Pageable pageable);

  List<Lesson> findLessonsByCourse(Course course);
  List<Lesson> findLessonByCourseOrderBySerial(Course course);
}

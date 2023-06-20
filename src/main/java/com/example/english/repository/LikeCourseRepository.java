package com.example.english.repository;

import com.example.english.entities.Course;
import com.example.english.entities.LikeCourse;
import com.example.english.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeCourseRepository extends JpaRepository<LikeCourse, Long> {
  Optional<LikeCourse> findLikeCourseByCourseAndUser(Course course, User user);
  List<LikeCourse> findLikeCoursesByCourse(Course course);
}

package com.example.english.repository;

import com.example.english.entities.Course;
import com.example.english.entities.Feedback;
import com.example.english.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
  List<Feedback> findFeedbacksByUser(User user);
  Page<Feedback> findFeedbacksByCourse(Pageable pageable, Course course);
  List<Feedback> findFeedbacksByCourse(Course course);
  Page<Feedback> findFeedbacksByMainFeedback(Pageable pageable, Feedback feedback);
}

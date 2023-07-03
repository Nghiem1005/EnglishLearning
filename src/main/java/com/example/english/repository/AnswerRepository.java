package com.example.english.repository;

import com.example.english.entities.Answer;
import com.example.english.entities.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
  Optional<Answer> findAnswerByQuestionAndCorrectIsTrue(Question question);
  List<Answer> findAnswersByQuestion(Question question);
}

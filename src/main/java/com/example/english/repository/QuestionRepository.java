package com.example.english.repository;

import com.example.english.entities.Part;
import com.example.english.entities.Question;
import com.example.english.entities.QuestionPhrase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
  List<Question> findQuestionsByQuestionPhrase(QuestionPhrase questionPhrase);
}

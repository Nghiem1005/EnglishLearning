package com.example.english.repository;

import com.example.english.entities.Practice;
import com.example.english.entities.Question;
import com.example.english.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
  Result findResultByPracticeAndQuestion(Practice practice, Question question);

}

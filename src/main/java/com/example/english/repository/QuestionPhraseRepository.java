package com.example.english.repository;

import com.example.english.entities.Part;
import com.example.english.entities.QuestionPhrase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPhraseRepository extends JpaRepository<QuestionPhrase, Long> {
  List<QuestionPhrase> findQuestionPhrasesByPart(Part part);
}

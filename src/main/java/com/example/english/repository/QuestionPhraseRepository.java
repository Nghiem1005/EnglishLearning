package com.example.english.repository;

import com.example.english.entities.QuestionPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPhraseRepository extends JpaRepository<QuestionPhrase, Long> {

}

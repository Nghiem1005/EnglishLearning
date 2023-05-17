package com.example.english.repository;

import com.example.english.entities.ListWord;
import com.example.english.entities.Word;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
  List<Word> findWordsByListWord(ListWord listWord);
}

package com.example.english.repository;

import com.example.english.entities.ListWord;
import com.example.english.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListWordRepository extends JpaRepository<ListWord, Long> {
  Page<ListWord> findListWordsByUser(Pageable pageable, User user);

}

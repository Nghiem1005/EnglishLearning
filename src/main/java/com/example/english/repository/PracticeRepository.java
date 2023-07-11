package com.example.english.repository;

import com.example.english.entities.Practice;
import com.example.english.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
  Page<Practice> findPracticesByUser(User user, Pageable pageable);
  List<Practice> findPracticesByUser(User user);

}

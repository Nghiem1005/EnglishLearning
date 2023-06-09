package com.example.english.repository;

import com.example.english.entities.Exam;
import com.example.english.entities.Part;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
  List<Part> findPartsByExam(Exam exam);
}

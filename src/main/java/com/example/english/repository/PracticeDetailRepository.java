package com.example.english.repository;

import com.example.english.entities.Part;
import com.example.english.entities.Practice;
import com.example.english.entities.PracticeDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeDetailRepository extends JpaRepository<PracticeDetail, Long> {
  List<PracticeDetail> findPracticeDetailsByPart(Part part);
  List<PracticeDetail> findPracticeDetailsByPractice(Practice practice);
}

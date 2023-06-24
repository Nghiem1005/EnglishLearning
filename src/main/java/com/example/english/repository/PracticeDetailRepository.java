package com.example.english.repository;

import com.example.english.entities.PracticeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeDetailRepository extends JpaRepository<PracticeDetail, Long> {

}

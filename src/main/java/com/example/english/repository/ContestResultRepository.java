package com.example.english.repository;

import com.example.english.entities.ContestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestResultRepository extends JpaRepository<ContestResult, Long> {

}

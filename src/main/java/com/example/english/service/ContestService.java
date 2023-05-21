package com.example.english.service;

import com.example.english.dto.request.ContestRequestDTO;
import com.example.english.dto.request.PartResultRequestDTO;
import com.example.english.dto.request.QuestionResponse;
import com.example.english.entities.Contest;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ContestService {
  ResponseEntity<?> createContest(ContestRequestDTO contestRequestDTO);
  ResponseEntity<?> updateResult(Long contestId, Long userId, float point);
  ResponseEntity<?> markContest(Long contestId, Long userId, List<PartResultRequestDTO> partResultRequestDTOS);
  ResponseEntity<?> getContestById(Long contestId);
  ResponseEntity<?> deleteContest(Long id);
}

package com.example.english.service;

import com.example.english.dto.request.TargetRequestDTO;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TargetService {
  ResponseEntity<?> createTarget(Long userId, TargetRequestDTO targetRequestDTO);

  ResponseEntity<?> getTargetByUser(Long userID);

  ResponseEntity<?> updateTarget(Long targetId);

  ResponseEntity<?> deleteTarget(Long id);
}

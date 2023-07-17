package com.example.english.service.impl;

import com.example.english.dto.request.TargetRequestDTO;
import com.example.english.service.TargetService;
import org.springframework.http.ResponseEntity;


public class TargetServiceImpl implements TargetService {

  @Override
  public ResponseEntity<?> createTarget(Long userId, TargetRequestDTO targetRequestDTO) {

    return null;
  }

  @Override
  public ResponseEntity<?> getTargetByUser(Long userID) {
    return null;
  }

  @Override
  public ResponseEntity<?> updateTarget(Long targetId) {
    return null;
  }

  @Override
  public ResponseEntity<?> deleteTarget(Long id) {
    return null;
  }
}

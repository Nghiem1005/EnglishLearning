package com.example.english.controller;

import com.example.english.dto.request.AnswerRequestDTO;
import com.example.english.dto.request.TargetRequestDTO;
import com.example.english.service.TargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/target")
public class TargetController {
  @Autowired private TargetService targetService;

  @PostMapping(value = "")
  public ResponseEntity<?> createTarget(@RequestBody TargetRequestDTO targetRequestDTO, @RequestParam(name = "userId") Long userId) {
    return targetService.createTarget(userId, targetRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<?> updateTarget(@RequestBody TargetRequestDTO targetRequestDTO,
      @RequestParam(name = "targetId") Long targetId) {
    return targetService.updateTarget(targetId, targetRequestDTO);
  }

  @GetMapping(value = "/user")
  public ResponseEntity<?> getTargetByUser(@RequestParam(name = "userId") Long userId) {
    return targetService.getTargetByUser(userId);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteTarget(@RequestParam(name = "id") Long id) {
    return targetService.deleteTarget(id);
  }
}

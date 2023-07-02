package com.example.english.controller;

import com.example.english.dto.request.LessonRequestDTO;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/notify")
public class NotificationController {

  @PostMapping(value = "")
  public ResponseEntity<?> createNotify(@RequestParam(name = "userId") Long userId,
      @ModelAttribute LessonRequestDTO lessonRequestDTO) throws IOException {
    return null;
  }
}

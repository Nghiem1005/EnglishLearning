package com.example.english.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussRequestDTO {
  private Long studentId;
  private Long subjectId;
  private String content;
  private MultipartFile[] images;
  private boolean pending = true;
  private Long mainDiscuss;
}

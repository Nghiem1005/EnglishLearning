package com.example.english.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestDTO {
  private Long studentId;
  private Long courseId;
  private String content;
  private MultipartFile[] images;
  private Long mainDiscuss;
}

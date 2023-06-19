package com.example.english.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordRequestDTO {
  private String content;
  private String define;
  private String spelling;
  private String type;
  private String example;
  private MultipartFile image;
}

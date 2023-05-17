package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordResponseDTO {
  private Long id;
  private String content;
  private String define;
  private String image;
  private String example;
}

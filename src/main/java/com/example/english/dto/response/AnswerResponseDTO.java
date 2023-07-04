package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDTO {
  private Long id;
  private String content;
  private boolean correct;
  private int serial;
  private String explain;
}

package com.example.english.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPhraseResponseDTO {
  private Long id;
  private int serial;
  private List<String> document;
  private List<QuestionResponseDTO> questionResponseDTOS;
}

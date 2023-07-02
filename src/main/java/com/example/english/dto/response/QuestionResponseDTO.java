package com.example.english.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDTO {
  private Long id;
  private String content;
  private int serial;
  private List<AnswerResponseDTO> answerResponseDTOS;
}

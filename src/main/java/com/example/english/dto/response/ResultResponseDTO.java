package com.example.english.dto.response;

import com.example.english.dto.request.QuestionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponseDTO {
  private Long questionId;
  private int serial;
  private int choice;
  private int answer;
  private boolean correct;
}

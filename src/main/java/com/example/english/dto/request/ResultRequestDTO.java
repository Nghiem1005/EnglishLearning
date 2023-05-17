package com.example.english.dto.request;

import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultRequestDTO {
  private Long questionId;
  private int choice;
}

package com.example.english.dto.request;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeRequestDTO {
  private String name;
  private int period;
  private String result;
  private List<ExerciseRequestDTO> exerciseRequestDTOS;
}

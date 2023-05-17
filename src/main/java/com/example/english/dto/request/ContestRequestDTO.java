package com.example.english.dto.request;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestRequestDTO {
  private String name;
  private Date date;
  private int period;
  private List<ExerciseRequestDTO> exerciseRequestDTOS;
}

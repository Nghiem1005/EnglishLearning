package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponseDTO {
  private int memberAmount;
  private double revenue;
  private int courseAmount;
  private int teacherAmount;
}

package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticByDayResponseDTO {
  private String time;
  private int studentCourse;
  private int newMember;
  private double revenueTotal;
}

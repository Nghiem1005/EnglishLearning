package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticInfoCourseResponseDTO {
  private int lessonAmount;
  private int feedbackAmount;
  private int likeAmount;
  private int studentAmount;
  private int documentAmount;
}

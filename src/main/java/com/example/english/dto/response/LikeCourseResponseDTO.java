package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeCourseResponseDTO {
  private Long studentId;
  private String studentName;
  private Long courseId;
  private String courseName;
}

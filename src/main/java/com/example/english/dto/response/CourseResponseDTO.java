package com.example.english.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {
  private Long id;

  private String name;

  private String[] document;

  private BigDecimal price;

  private String description;

  private String thumbnail;

  private UserResponseDTO teacher;

}

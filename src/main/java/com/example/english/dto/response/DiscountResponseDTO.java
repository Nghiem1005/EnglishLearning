package com.example.english.dto.response;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponseDTO {
  private Long id;
  private String content;
  private String code;
  private int percent;
  private Date startDate;
  private Date endDate;
  private List<CourseResponseDTO> courseResponseDTOS;
}

package com.example.english.dto.request;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequestDTO {
  private String content;
  private int percent;
  private Date endDate;
  private Date startDate;
  private List<Long> courseId;
}

package com.example.english.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDTO {
  private Long id;

  private BigDecimal price;

  private String paymentMethod;

  private Date createDate;

  private Long courseId;

  private Long studentId;

  private String studentName;
}

package com.example.english.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRequestDTO {
  private int price;
  private String description;
  private Long courseId;
  private Long studentId;
}

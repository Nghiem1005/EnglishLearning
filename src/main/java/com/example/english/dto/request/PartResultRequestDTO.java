package com.example.english.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartResultRequestDTO {
  private Long partId;
  private List<ResultRequestDTO> resultRequestDTOS;
}

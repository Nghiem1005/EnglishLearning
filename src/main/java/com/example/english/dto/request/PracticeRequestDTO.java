package com.example.english.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeRequestDTO {
  private int period;
  private List<PartResultRequestDTO> partResultRequestDTOS;
}

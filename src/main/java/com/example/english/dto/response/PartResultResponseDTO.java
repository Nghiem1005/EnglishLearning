package com.example.english.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartResultResponseDTO {
  private Long partId;
  private int serial;
  private String type;
  private List<ResultResponseDTO> resultResponseDTOS;
}

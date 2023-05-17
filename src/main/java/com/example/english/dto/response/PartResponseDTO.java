package com.example.english.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartResponseDTO {
  private Long id;
  private int serial;
  private String description;
  private String type;
  private String document;
  private List<QuestionResponseDTO> questionResponseDTOS;
}

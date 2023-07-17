package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetResponseDTO {
  private Long id;

  private String type;

  private float point;

  private UserResponseDTO userResponseDTO;
}

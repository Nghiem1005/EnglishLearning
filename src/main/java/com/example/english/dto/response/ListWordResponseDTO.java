package com.example.english.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWordResponseDTO {
  private Long id;
  private String name;
  private String description;
  private UserResponseDTO UserResponseDTO;
  private List<WordResponseDTO> wordResponseDTOS;
}

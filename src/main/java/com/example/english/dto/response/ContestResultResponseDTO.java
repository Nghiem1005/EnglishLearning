package com.example.english.dto.response;

import com.example.english.entities.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestResultResponseDTO {
  private Long contestId;
  private Long userId;
  private List<PartResultResponseDTO> partResultResponseDTOS;
  private int point;
}

package com.example.english.dto.response;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeResponseDTO {
  private Long id;
  private int period;
  private Date createDate;
  private String result;
  private List<PartResultResponseDTO> partResultResponseDTOS;
  private UserResponseDTO userResponseDTO;
}

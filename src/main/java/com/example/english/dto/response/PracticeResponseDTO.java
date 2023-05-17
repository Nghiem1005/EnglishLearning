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
  private String name;
  private int period;
  private Date date;
  private String result;
  List<PartResponseDTO> partResponseDTOS;
  private UserResponseDTO userResponseDTO;
}

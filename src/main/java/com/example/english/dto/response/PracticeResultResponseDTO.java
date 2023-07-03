package com.example.english.dto.response;

import java.util.Date;
import java.util.List;
import javax.xml.crypto.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeResultResponseDTO {
  private Long examId;
  private String examName;
  private Long practiceId;
  private String result;
  private int period;
  private Date createDate;
  private List<PartResultResponseDTO> partResultResponseDTOS;
}

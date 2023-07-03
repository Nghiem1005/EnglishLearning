package com.example.english.dto.response;

import java.util.List;
import javax.xml.crypto.Data;

public class PracticeResultResponseDTO {
  private Long examId;
  private String examName;
  private Long practiceId;
  private String result;
  private int period;
  private Data date;
  private List<ResultResponseDTO> resultResponseDTOS;
}

package com.example.english.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPhraseRequestDTO {
  private int serial;
  private List<String> image;
  private List<QuestionRequestDTO> questionRequestDTOS;
}

package com.example.english.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRequestDTO {
 private String description;
 private String type;
 private MultipartFile document;
 private int serial;
 private List<QuestionRequestDTO> questionRequestDTOS;
}

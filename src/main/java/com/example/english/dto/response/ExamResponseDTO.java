package com.example.english.dto.response;

import com.example.english.entities.Exam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponseDTO {
 private Long id;
 private String name;
 private int period;
 private String type;
 private List<PartResponseDTO> partResponseDTOS;
}

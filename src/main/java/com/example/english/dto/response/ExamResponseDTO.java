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
 private Long lessonId;
 private PartResponseDTO partResponseDTO;

 public ExamResponseDTO(Long lessonId, Long partId, int serial, String partDescription,
     String partType, String document) {
  this.lessonId = lessonId;
  this.partResponseDTO.setId(partId);
  this.partResponseDTO.setSerial(serial);
  this.partResponseDTO.setDescription(partDescription);
  this.partResponseDTO.setType(partType);
  this.partResponseDTO.setDocument(document);
 }

 public ExamResponseDTO build(Exam exam) {
  return new ExamResponseDTO(exam.getLesson().getId(), exam.getPart().getId(), exam.getSerial(), exam.getPart().getDescription(), exam.getPart().getType().toString(), exam.getPart().getDocument());
 }
}

package com.example.english.dto.response;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussResponseDTO {
  private Long id;
  private String content;
  private Long studentId;
  private String studentName;
  private Long subjectId;
  private String subjectName;
  private Date createDate;
  private Date updateDate;
  private DiscussResponseDTO mainDiscuss;
}
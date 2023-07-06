package com.example.english.dto.response;

import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussResponseDTO {
  private Long id;
  private String content;
  private Long userId;
  private String userName;
  private String userRole;
  private Long subjectId;
  private String subjectName;
  private List<String> images;
  private Date createDate;
  private Date updateDate;
  private boolean pending;
  private DiscussResponseDTO mainDiscuss;
}
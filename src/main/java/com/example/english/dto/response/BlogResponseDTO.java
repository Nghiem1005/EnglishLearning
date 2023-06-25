package com.example.english.dto.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponseDTO {
 private Long id;
 private String title;
 private String content;
 private String description;
 private String image;
 private Date createDate;
 private Date updateDate;
 private UserResponseDTO userResponseDTO;
}

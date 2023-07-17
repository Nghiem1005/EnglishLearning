package com.example.english.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
  @NotNull(message = "Course name is required")
  private String name;

  private MultipartFile[] documents;

  private String type;

  private int pointTarget;

  private BigDecimal price;

  private String description;

  private MultipartFile thumbnail;
}

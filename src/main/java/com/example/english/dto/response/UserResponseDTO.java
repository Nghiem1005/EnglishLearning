package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
  private Long id;
  private String name;
  private String email;
  private String phone;
  private boolean enable;
  private String description;
  private String image;
  private String role;
  private String provider;
}

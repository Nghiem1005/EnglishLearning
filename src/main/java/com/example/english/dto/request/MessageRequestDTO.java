package com.example.english.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
  private Long senderId;
  private Long receiverId;
  private String message;
  private String date;
  private String status;
}

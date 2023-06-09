package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
  private String message;
  private String date;
  private String status;
}

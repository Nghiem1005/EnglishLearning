package com.example.english.service;

import com.example.english.dto.request.MessageRequestDTO;
import com.example.english.entities.Message;
import org.springframework.http.ResponseEntity;

public interface MessageService {
  ResponseEntity<?> createMessage(MessageRequestDTO messageRequestDTO);
  ResponseEntity<?> getAllReceiverBySender(Long senderId);
  ResponseEntity<?> getAllMessageBySenderAndReceiver(Long senderId, Long receiverId);
}

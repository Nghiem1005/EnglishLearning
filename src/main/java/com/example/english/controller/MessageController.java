package com.example.english.controller;

import com.example.english.dto.request.MessageRequestDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Message;
import com.example.english.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MessageController {
  @Autowired private MessageService messageService;

  @MessageMapping("/private-message")
  public ResponseEntity<?> recMessage(@RequestBody MessageRequestDTO messageRequestDTO){
    return messageService.createMessage(messageRequestDTO);
  }

  @GetMapping("/message/room")
  public ResponseEntity<?> getAllMessageBySenderAndReceiver(@RequestParam(name = "senderId") Long senderId, @RequestParam(name = "receiverId") Long receiverId) {
    return messageService.getAllMessageBySenderAndReceiver(senderId, receiverId);
  }

  @GetMapping("/message/sender")
  public ResponseEntity<?> getAllMessageBySender(@RequestParam(name = "senderId") Long senderId) {
    return messageService.getAllReceiverBySender(senderId);
  }
}

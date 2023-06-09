package com.example.english.service.impl;

import com.example.english.dto.request.MessageRequestDTO;
import com.example.english.dto.response.MessageResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Message;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.MessageMapper;
import com.example.english.repository.MessageRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
  @Autowired private UserRepository userRepository;
  @Autowired private MessageRepository messageRepository;
  @Autowired private SimpMessagingTemplate simpMessagingTemplate;
  @Override
  public ResponseEntity<?> createMessage(MessageRequestDTO messageRequestDTO) {
    User sender = userRepository.findById(messageRequestDTO.getSenderId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find sender with ID = " + messageRequestDTO.getSenderId()));

    User receiver = userRepository.findById(messageRequestDTO.getReceiverId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find receiver with ID = " + messageRequestDTO.getReceiverId()));

    Message message = new Message();
    message.setContent(messageRequestDTO.getMessage());
    message.setStatus(messageRequestDTO.getStatus());
    message.setReceiverUser(receiver);
    message.setSendUser(sender);

    Message messageSaved = messageRepository.save(message);

    MessageResponseDTO messageResponseDTO = MessageMapper.INSTANCE.messageToMessageResponseDTO(messageSaved);

    simpMessagingTemplate.convertAndSendToUser(message.getReceiverUser().getName(),"/private",messageResponseDTO);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add message success", messageResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllReceiverBySender(Long senderId) {
    User sender = userRepository.findById(senderId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find sender with ID = " + senderId));

    List<Message> messageList = messageRepository.findMessagesBySendUser(senderId);

    List<MessageResponseDTO> messageResponseDTOList = convertListMessageToListMessageResponseDTO(messageList);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get all message", messageResponseDTOList));
  }

  @Override
  public ResponseEntity<?> getAllMessageBySenderAndReceiver(Long senderId, Long receiverId) {
    User sender = userRepository.findById(senderId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find sender with ID = " + senderId));

    User receiver = userRepository.findById(receiverId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find receiver with ID = " + receiverId));

    List<Message> messageList = messageRepository.findMessagesBySendUserAndReceiverUser(sender, receiver);

    List<MessageResponseDTO> messageResponseDTOList = convertListMessageToListMessageResponseDTO(messageList);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get all message", messageResponseDTOList));
  }

  public List<MessageResponseDTO> convertListMessageToListMessageResponseDTO(List<Message> messageList) {
    List<MessageResponseDTO> messageResponseDTOList = new ArrayList<>();
    for (Message message : messageList) {
      MessageResponseDTO messageResponseDTO = MessageMapper.INSTANCE.messageToMessageResponseDTO(message);
      messageResponseDTOList.add(messageResponseDTO);
    }
    return messageResponseDTOList;
  }
}

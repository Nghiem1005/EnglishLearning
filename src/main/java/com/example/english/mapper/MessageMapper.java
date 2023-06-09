package com.example.english.mapper;

import com.example.english.dto.response.MessageResponseDTO;
import com.example.english.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface MessageMapper {
  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  @Mapping(target = "senderId", source = "message.sendUser.id")
  @Mapping(target = "senderName", source = "message.sendUser.name")
  @Mapping(target = "receiverId", source = "message.receiverUser.id")
  @Mapping(target = "receiverName", source = "message.receiverUser.name")
  @Mapping(target = "date", source = "message.createDate")
  MessageResponseDTO messageToMessageResponseDTO(Message message);
}

package com.example.english.mapper;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.entities.Comment;
import com.example.english.entities.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  CommentMapper INSTANCE = Mappers.getMapper( CommentMapper.class );
  @Mapping(target = "images", expression = "java(null)")
  Comment commentRequestDTOToComment(DiscussRequestDTO comment);
  DiscussResponseDTO commentToCommentResponseDTO(Comment comment);
}

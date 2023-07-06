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

  @Mapping(target = "subjectId", source = "c.blog.id")
  @Mapping(target = "subjectName", source = "c.blog.title")
  @Mapping(target = "userId", source = "c.user.id")
  @Mapping(target = "userName", source = "c.user.name")
  @Mapping(target = "userRole", source = "c.user.role")
  DiscussResponseDTO commentToCommentResponseDTO(Comment c);
}

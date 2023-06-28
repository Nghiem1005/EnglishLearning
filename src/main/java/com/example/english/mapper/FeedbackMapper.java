package com.example.english.mapper;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.entities.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
  FeedbackMapper INSTANCE = Mappers.getMapper( FeedbackMapper.class );
  @Mapping(target = "images", expression = "java(null)")
  Feedback feedbackRequestDTOToFeedback(DiscussRequestDTO feedback);

  @Mapping(target = "subjectId", source = "c.course.id")
  @Mapping(target = "subjectName", source = "c.course.name")
  @Mapping(target = "studentId", source = "c.user.id")
  @Mapping(target = "studentName", source = "c.user.name")
  DiscussResponseDTO feedbackToFeedbackResponseDTO(Feedback c);
}

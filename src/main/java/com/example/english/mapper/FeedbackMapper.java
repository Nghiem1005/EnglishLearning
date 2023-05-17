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
  DiscussResponseDTO feedbackToFeedbackResponseDTO(Feedback feedback);
}

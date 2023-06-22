package com.example.english.mapper;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.entities.Inquiry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InquiryMapper {
  InquiryMapper INSTANCE = Mappers.getMapper( InquiryMapper.class );
  @Mapping(target = "images", expression = "java(null)")
  Inquiry inquiryRequestDTOToInquiry(DiscussRequestDTO inquiry);

  @Mapping(target = "subjectId", source = "c.lesson.id")
  @Mapping(target = "subjectName", source = "c.lesson.name")
  @Mapping(target = "studentId", source = "c.user.id")
  @Mapping(target = "studentName", source = "c.user.name")
  DiscussResponseDTO inquiryToInquiryResponseDTO(Inquiry c);
}

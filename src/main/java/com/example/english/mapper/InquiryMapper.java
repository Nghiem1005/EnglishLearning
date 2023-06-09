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
  @Mapping(target = "userId", source = "c.user.id")
  @Mapping(target = "userName", source = "c.user.name")
  @Mapping(target = "userRole", source = "c.user.role")
  DiscussResponseDTO inquiryToInquiryResponseDTO(Inquiry c);
}

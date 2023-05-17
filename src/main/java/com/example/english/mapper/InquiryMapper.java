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
  DiscussResponseDTO inquiryToInquiryResponseDTO(Inquiry inquiry);
}

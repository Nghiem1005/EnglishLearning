package com.example.english.mapper;

import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.response.PracticeResponseDTO;
import com.example.english.dto.response.PracticeResultResponseDTO;
import com.example.english.entities.Practice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PracticeMapper {
  PracticeMapper INSTANCE = Mappers.getMapper(PracticeMapper.class);

  Practice practiceRequestDTOToPractice(PracticeRequestDTO practiceRequestDTO);
  PracticeResponseDTO practiceToPracticeResponseDTO(Practice practice);

  @Mapping(source = "id", target = "practiceId")
  PracticeResultResponseDTO practiceToPracticeResultResponseDTO(Practice practice);
}

package com.example.english.mapper;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.dto.response.ExamResponseDTO;
import com.example.english.entities.Exam;
import com.example.english.entities.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamMapper {
  ExamMapper INSTANCE = Mappers.getMapper( ExamMapper.class );

  Exam examRequestDTOToExam(ExamRequestDTO e);

  ExamResponseDTO examToExamResponseDTO(Exam e);
}

package com.example.english.mapper;

import com.example.english.dto.request.QuestionResponse;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
  QuestionMapper INSTANCE  = Mappers.getMapper(QuestionMapper.class);

  QuestionResponseDTO questionToQuestionResponseDTO(Question question);
}

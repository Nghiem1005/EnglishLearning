package com.example.english.mapper;

import com.example.english.dto.response.AnswerResponseDTO;
import com.example.english.entities.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
  AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

  AnswerResponseDTO answerToAnswerResponseDTO(Answer answer);
}

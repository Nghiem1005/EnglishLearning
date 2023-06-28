package com.example.english.mapper;

import com.example.english.dto.response.AnswerResponseDTO;
import com.example.english.dto.response.ResultResponseDTO;
import com.example.english.entities.Answer;
import com.example.english.entities.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResultMapper {
  ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

  @Mapping(target = "questionId", source = "result.question.id")
  ResultResponseDTO resultToResultResponseDTO(Result result);
}

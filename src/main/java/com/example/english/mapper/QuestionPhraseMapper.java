package com.example.english.mapper;

import com.example.english.dto.response.QuestionPhraseResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.entities.Question;
import com.example.english.entities.QuestionPhrase;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionPhraseMapper {
  QuestionPhraseMapper INSTANCE  = Mappers.getMapper(QuestionPhraseMapper.class);

  QuestionPhraseResponseDTO questionPhraseToQuestionPhraseResponseDTO(QuestionPhrase questionPhrase);
}

package com.example.english.mapper;

import com.example.english.dto.request.WordRequestDTO;
import com.example.english.dto.response.WordResponseDTO;
import com.example.english.entities.Word;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WordMapper {
  WordMapper INSTANCE = Mappers.getMapper(WordMapper.class);

  Word wordRequestDTOToWord(WordRequestDTO wordRequestDTO);
  WordResponseDTO wordToWordResponseDTO(Word word);
}

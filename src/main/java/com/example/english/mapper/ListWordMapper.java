package com.example.english.mapper;

import com.example.english.dto.request.ListWordRequestDTO;
import com.example.english.dto.response.ListWordResponseDTO;
import com.example.english.entities.ListWord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ListWordMapper {
  ListWordMapper INSTANCE = Mappers.getMapper(ListWordMapper.class);

  ListWord listWordRequestDTOToListWord(ListWordRequestDTO listWordRequestDTO);
  ListWordResponseDTO listWordDTOToListWordResponse(ListWord listWord);
}

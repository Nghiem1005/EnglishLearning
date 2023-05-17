package com.example.english.mapper;

import com.example.english.dto.request.ContestRequestDTO;
import com.example.english.dto.response.ContestResponseDTO;
import com.example.english.entities.Contest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContestMapper {
  ContestMapper INSTANCE = Mappers.getMapper(ContestMapper.class);

  Contest contestRequestDTOToContest(ContestRequestDTO contestRequestDTO);

  ContestResponseDTO contestToContestResponseDTO(Contest contest);
}

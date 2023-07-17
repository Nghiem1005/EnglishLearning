package com.example.english.mapper;

import com.example.english.dto.request.TargetRequestDTO;
import com.example.english.dto.response.TargetResponseDTO;
import com.example.english.entities.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TargetMapper {
  TargetMapper INSTANCE = Mappers.getMapper(TargetMapper.class);

  Target targetRequestDTOToTarget(TargetRequestDTO c);

  TargetResponseDTO targetToTargetResponseDTO(Target c);
}

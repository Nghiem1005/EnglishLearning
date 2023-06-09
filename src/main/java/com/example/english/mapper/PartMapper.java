package com.example.english.mapper;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.entities.Part;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PartMapper {
  PartMapper INSTANCE = Mappers.getMapper(PartMapper.class);

  Part partRequestDTOToPart(PartRequestDTO partRequestDTO);
  PartResponseDTO partToPartResponseDTO(Part part);
}

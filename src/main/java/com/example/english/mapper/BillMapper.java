package com.example.english.mapper;

import com.example.english.dto.response.BillResponseDTO;
import com.example.english.entities.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillMapper {
  BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

  BillResponseDTO billToBillResponseDTO(Bill bill);
}

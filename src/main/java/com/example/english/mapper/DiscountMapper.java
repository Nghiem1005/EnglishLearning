package com.example.english.mapper;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.response.DiscountResponseDTO;
import com.example.english.entities.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
  DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);

  Discount discountRequestDTOToDiscount(DiscountRequestDTO dto);
  DiscountResponseDTO discountToDiscountResponseDTO(Discount discount);
}

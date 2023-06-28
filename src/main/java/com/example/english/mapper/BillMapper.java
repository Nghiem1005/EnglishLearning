package com.example.english.mapper;

import com.example.english.dto.response.BillResponseDTO;
import com.example.english.entities.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillMapper {
  BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

  @Mapping(target = "courseId", source = "bill.course.id")
  @Mapping(target = "studentId", source = "bill.user.id")
  @Mapping(target = "studentName", source = "bill.user.id")
  BillResponseDTO billToBillResponseDTO(Bill bill);
}

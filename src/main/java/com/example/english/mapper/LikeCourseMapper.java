package com.example.english.mapper;

import com.example.english.dto.response.LikeCourseResponseDTO;
import com.example.english.entities.LikeCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LikeCourseMapper {
  LikeCourseMapper INSTANCE = Mappers.getMapper(LikeCourseMapper.class);
  @Mapping(target = "studentId", source = "c.user.id")
  @Mapping(target = "studentName", source = "c.user.name")
  @Mapping(target = "courseId", source = "c.course.id")
  @Mapping(target = "courseName", source = "c.course.name")
  LikeCourseResponseDTO likeCourseToLikeCourseResponseDTO(LikeCourse c);
}

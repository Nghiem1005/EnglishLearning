package com.example.english.mapper;

import com.example.english.dto.request.CourseRequestDTO;
import com.example.english.dto.response.CourseResponseDTO;
import com.example.english.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CoursesMapper {
  CoursesMapper INSTANCE = Mappers.getMapper(CoursesMapper.class);
  @Mapping(target = "thumbnail", expression = "java(null)")
  Course courseRequestDTOToCourse(CourseRequestDTO c);

  CourseResponseDTO courseToCourseResponseDTO(Course c);
}

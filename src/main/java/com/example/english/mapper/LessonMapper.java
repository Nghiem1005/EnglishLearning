package com.example.english.mapper;

import com.example.english.dto.request.LessonRequestDTO;
import com.example.english.dto.response.LessonResponseDTO;
import com.example.english.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LessonMapper{
  LessonMapper INSTANCE = Mappers.getMapper( LessonMapper.class );

  @Mapping(target = "video", expression = "java(null)")
  Lesson lessonRequestDTOToLesson(LessonRequestDTO c);

  @Mapping(target = "document", source = "video")
  @Mapping(target = "courseId", source = "c.course.id")
  @Mapping(target = "courseName", source = "c.course.name")
  @Mapping(target = "teacherId", source = "c.course.teacher.id")
  @Mapping(target = "teacherName", source = "c.course.teacher.name")
  LessonResponseDTO lessonToLessonResponseDTO(Lesson c);
}

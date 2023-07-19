package com.example.english.service.impl;

import com.example.english.dto.request.LessonRequestDTO;
import com.example.english.dto.response.LessonResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Course;
import com.example.english.entities.Lesson;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.LessonMapper;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.LessonRepository;
import com.example.english.service.LessonService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private StorageService storageService;

  @Override
  public ResponseEntity<?> getAllLessonByCourse(Pageable pageable, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Page<Lesson> lessonPage = lessonRepository.findLessonsByCourse(course, pageable);
    List<Lesson> lessonList = lessonPage.getContent();
    List<LessonResponseDTO> lessonResponseDTOArrayList = new ArrayList<>();
    for (Lesson lesson : lessonList) {
      LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lesson);
      lessonResponseDTOArrayList.add(lessonResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", lessonResponseDTOArrayList,
        lessonPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createLesson(Long courseId,
      LessonRequestDTO lessonRequestDTO) throws IOException {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Lesson lesson = LessonMapper.INSTANCE.lessonRequestDTOToLesson(lessonRequestDTO);
    lesson.setCourse(course);

    if (lessonRequestDTO.getVideo() != null){
      lesson.setVideo(storageService.uploadFile(lessonRequestDTO.getVideo()));
    }

    //Check serial greater previous serial and consecutive
    List<Lesson> lessonList = lessonRepository.findLessonByCourseOrderBySerial(course);
    if (lessonList.size() != 0){
      lesson.setSerial(lessonList.get(lessonList.size() - 1).getSerial() + 1);
    } else {
      lesson.setSerial(1);
    }

    LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lessonRepository.save(lesson));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create lesson success!", lessonResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateLesson(Long id, LessonRequestDTO lessonRequestDTO)
      throws IOException {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + id));

    lesson.setName(lessonRequestDTO.getName());

    if (lessonRequestDTO.getDescription() != null) {
      lesson.setDescription(lessonRequestDTO.getDescription());
    }
    //lesson.setDocument(lessonRequestDTO.getDocument());

    if (lessonRequestDTO.getVideo() != null){
      lesson.setVideo(storageService.uploadFile(lessonRequestDTO.getVideo()));
    }
    Lesson lessonSaved = lessonRepository.save(lesson);
    LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lessonSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create lesson success!", lessonResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteLesson(Long id) {
    Lesson getLesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));

    lessonRepository.delete(getLesson);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete lesson successfully!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getLessonById(Long id) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + id));
    LessonResponseDTO lessonResponseDTO = LessonMapper.INSTANCE.lessonToLessonResponseDTO(lesson);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get lesson.", lessonResponseDTO));
  }
}

package com.example.english.service.impl;

import com.example.english.dto.response.LikeCourseResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Course;
import com.example.english.entities.LikeCourse;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.LikeCourseMapper;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.LikeCourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.LikeCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeCourseServiceImpl implements LikeCourseService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private LikeCourseRepository likeCourseRepository;
  @Override
  public ResponseEntity<?> createLikeCourse(Long studentId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    LikeCourse likeCourse = new LikeCourse();
    likeCourse.setCourse(course);
    likeCourse.setUser(student);

    LikeCourseResponseDTO likeCourseResponseDTO = LikeCourseMapper.INSTANCE
        .likeCourseToLikeCourseResponseDTO(likeCourseRepository.save(likeCourse));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add student to course success!", likeCourseResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteLikeCourse(Long studentId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    LikeCourse getStudentCourse = likeCourseRepository.findLikeCourseByCourseAndUser(course, student)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId + " and student ID = " + studentId));

    likeCourseRepository.delete(getStudentCourse);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete student to course success!"));
  }
}

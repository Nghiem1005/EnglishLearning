package com.example.english.controller;

import com.example.english.dto.request.CourseRequestDTO;
import com.example.english.service.CourseService;
import com.example.english.service.LikeCourseService;
import com.example.english.service.StorageService;
import com.example.english.service.StudentCourseService;
import com.example.english.utils.Converter;
import com.example.english.utils.Utils;
import com.google.cloud.storage.Blob;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/course")
public class CourseController {
  @Autowired
  private CourseService courseService;
  @Autowired private StudentCourseService studentCourseService;

  @Autowired private StorageService storageService;
  @Autowired private LikeCourseService likeCourseService;

  @PostMapping(value = "{teacherId}")
  public ResponseEntity<?> createCourse(@PathVariable(name = "teacherId") Long teacherId,@ModelAttribute @Valid CourseRequestDTO courseRequestDTO)
      throws IOException {
    return courseService.saveCourse(teacherId, courseRequestDTO);
  }

  @PutMapping(value = "{courseId}")
  public ResponseEntity<?> updateCourse(@PathVariable(name = "courseId") Long courseId,@ModelAttribute @Valid CourseRequestDTO courseRequestDTO)
      throws IOException {
    return courseService.updateCourse(courseId, courseRequestDTO);
  }

 @GetMapping(value = "")
  public ResponseEntity<?> getAllCourse(@RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return courseService.getAllCourse(pageable);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<?> getCourseById(@PathVariable(name = "id") Long id) {
    return courseService.getCourseById(id);
  }

  @GetMapping(value = "/teacher/{teacherId}")
  public ResponseEntity<?> getCourseByTeacher(@PathVariable(name = "teacherId") Long teacherId,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return courseService.getCourseByTeacher(pageable, teacherId);
  }

  /*@GetMapping(value = "/teacher/blank")
  public ResponseEntity<?> getCourseByTeacher(
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return courseService.getCourseByNotTeacher(pageable);
  }

  @GetMapping(value = "/type")
  public ResponseEntity<?> getCourseByTeacher(@RequestParam(name = "type") String type,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return courseService.getCourseByType(pageable, type);
  }*/

  @PostMapping(value = "/student")
  public ResponseEntity<?> createStudentCourse(@RequestParam(name = "studentId") Long studentId,@RequestParam(name = "courseId") Long courseId) {
    return studentCourseService.createStudentCourse(studentId, courseId);
  }

  @DeleteMapping(value = "{courseId}")
  public ResponseEntity<?> deleteCourse(@PathVariable(name = "courseId") Long courseId) {
    return courseService.deleteCourse(courseId);
  }

  @GetMapping(value = "/student")
  public ResponseEntity<?> getCourseByStudent(@RequestParam(name = "studentId") Long studentId,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return studentCourseService.getCourseByStudent(pageable, studentId);
  }

  @PutMapping(value = "/student")
  public ResponseEntity<?> getCourseByStudent(@RequestParam(name = "studentId") Long studentId, @RequestParam(name = "courseId") Long courseId,
      @RequestParam(name = "progress") int progress) {
    return studentCourseService.updateProgress(studentId, courseId, progress);
  }

  @GetMapping(value = "/document/{fileName}")
  public ResponseEntity getImageUser(@PathVariable("fileName") String fileName) {
    Blob blob = storageService.getFile(fileName);
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + fileName + "\"")
        .body(Converter.blobToResource(blob));
  }

  @PostMapping(value = "/like")
  public ResponseEntity<?> createLikeCourse(@RequestParam(name = "studentId") Long studentId,@RequestParam(name = "courseId") Long courseId) {
    return likeCourseService.createLikeCourse(studentId, courseId);
  }

  @DeleteMapping(value = "/like")
  public ResponseEntity<?> deleteLikeCourse(@RequestParam(name = "studentId") Long studentId,@RequestParam(name = "courseId") Long courseId) {
    return likeCourseService.deleteLikeCourse(studentId, courseId);
  }
}

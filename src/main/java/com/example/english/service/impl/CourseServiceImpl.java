package com.example.english.service.impl;

import com.example.english.dto.request.CourseRequestDTO;
import com.example.english.dto.response.CourseResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Bill;
import com.example.english.entities.Course;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.CoursesMapper;
import com.example.english.repository.BillRepository;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.CourseService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CourseServiceImpl implements CourseService {
  @Autowired private UserRepository userRepository;
  @Autowired private StorageService storageService;
  @Autowired private CourseRepository courseRepository;
  @Autowired private BillRepository billRepository;
  @Override
  public ResponseEntity<?> saveCourse(Long teacherId, CourseRequestDTO courseRequestDTO)
      throws IOException {
    Course course = CoursesMapper.INSTANCE.courseRequestDTOToCourse(courseRequestDTO);

    User teacher = userRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + teacherId));
    course.setTeacher(teacher);

    //Store document
    if (courseRequestDTO.getDocuments() != null){
      List<String> nameFiles = storeFile(courseRequestDTO.getDocuments());
      course.setDocument(nameFiles);
    }

    //Store thumbnail
    if (courseRequestDTO.getThumbnail() != null){
      String fileName = storageService.uploadFile(courseRequestDTO.getThumbnail());
      course.setThumbnail(storageService.getFile(fileName).getMediaLink());
    }

    Course courseSaved = courseRepository.save(course);

    CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(courseSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create course success!", courseResponseDTO));
  }

  private List<String> storeFile(MultipartFile[] documents) throws IOException {
    int numberOfDocument = documents.length;
    String[] nameFiles = new String[numberOfDocument];
    for (int i=0; i<numberOfDocument; i++){
      nameFiles[i] = storageService.uploadFile(documents[i]);
    }

    return new ArrayList<>(Arrays.asList(nameFiles));
  }

  @Override
  public ResponseEntity<?> updateCourse(Long courseId, CourseRequestDTO courseRequestDTO)
      throws IOException {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    course.setPrice(courseRequestDTO.getPrice());
    course.setDescription(courseRequestDTO.getDescription());

    //Check name
    if (!courseRequestDTO.getName().equals(course.getName())){
      course.setName(courseRequestDTO.getName());
    }

    //Store document
    if (courseRequestDTO.getDocuments() != null){
      List<String> nameFiles = storeFile(courseRequestDTO.getDocuments());
      course.setDocument(nameFiles);
    }

    //Store thumbnail
    if (courseRequestDTO.getThumbnail() != null){
      String fileName = storageService.uploadFile(courseRequestDTO.getThumbnail());
      course.setThumbnail(storageService.getFile(fileName).getMediaLink());
    }

    Course courseSaved = courseRepository.save(course);

    CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(courseSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update course success!", courseResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteCourse(Long courseId) {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    //Update course on bill to null
    List<Bill> billList = billRepository.findBillsByCourse(course);
    for (Bill bill : billList){
      bill.setCourse(null);
      billRepository.save(bill);
    }
    deleteFile(course);

    courseRepository.delete(course);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete course success!"));
  }

  private void deleteFile(Course course) {
    for (String nameFile : course.getDocument()){
      storageService.deleteFile(nameFile);
    }
  }

  @Override
  public ResponseEntity<?> getAllCourse(Pageable pageable) {
    Page<Course> getCourseList = courseRepository.findAll(pageable);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList), getCourseList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getCourseById(Long id) {
    Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + id));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get a course!", CoursesMapper.INSTANCE.courseToCourseResponseDTO(course)));
  }

  @Override
  public ResponseEntity<?> getCourseByTeacher(Pageable pageable, Long teacherId) {
    User teacher = userRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + teacherId));
    Page<Course> getCourseList = courseRepository.findCoursesByTeacher(pageable, teacher);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList), getCourseList.getTotalPages()));
  }

  private List<CourseResponseDTO> toCourseResponseDTOList(List<Course> courseList){
    List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();
    for (Course course : courseList){
      courseResponseDTOList.add(CoursesMapper.INSTANCE.courseToCourseResponseDTO(course));
    }
    return courseResponseDTOList;
  }

  @Override
  public ResponseEntity<?> getCourseByNotTeacher(Pageable pageable) {
    return null;
  }

  @Override
  public ResponseEntity<?> getCourseByType(Pageable pageable, String type) {
    return null;
  }
}

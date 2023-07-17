package com.example.english.service.impl;

import com.example.english.dto.request.CourseRequestDTO;
import com.example.english.dto.response.CourseResponseDTO;
import com.example.english.dto.response.DiscountResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Bill;
import com.example.english.entities.Course;
import com.example.english.entities.DiscountDetail;
import com.example.english.entities.LikeCourse;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.CoursesMapper;
import com.example.english.mapper.DiscountMapper;
import com.example.english.repository.BillRepository;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.DiscountDetailRepository;
import com.example.english.repository.LikeCourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.CourseService;
import com.example.english.service.StorageService;
import com.example.english.specification.CourseSpecificationsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
  @Autowired private DiscountDetailRepository discountDetailRepository;
  @Autowired private LikeCourseRepository likeCourseRepository;
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
      course.setThumbnail(storageService.uploadFile(courseRequestDTO.getThumbnail()));
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
      course.setThumbnail(storageService.uploadFile(courseRequestDTO.getThumbnail()));
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
  public ResponseEntity<?> getAllCourse(Long userId, Pageable pageable) {
    Page<Course> getCourseList = courseRepository.findAll(pageable);
    List<Course> courseList = getCourseList.getContent();

    Optional<User> getUser = userRepository.findById(userId);
    User user = null;
    if (getUser.isPresent()) {
      user = getUser.get();
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList, user), getCourseList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getCourseById(Long id, Long userId) {
    Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + id));

    Optional<User> user = userRepository.findById(userId);
    CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(course);
    if (user.isPresent()) {
      Optional<LikeCourse> likeCourse = likeCourseRepository.findLikeCourseByCourseAndUser(course, user.get());

      if (likeCourse.isPresent()) {
        courseResponseDTO.setLike(true);
      }
    }
    //Get discount now of course
    Optional<DiscountDetail> discountDetail = discountDetailRepository.findDiscountByDayInPeriod(course.getId(), new Date());
    if (discountDetail.isPresent()) {
      DiscountResponseDTO discountResponseDTO = DiscountMapper.INSTANCE.discountToDiscountResponseDTO(discountDetail.get().getDiscount());
      courseResponseDTO.setDiscountResponseDTO(discountResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get a course!", courseResponseDTO));
  }

  @Override
  public ResponseEntity<?> getCourseByTeacher(Pageable pageable, Long teacherId) {
    User teacher = userRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + teacherId));
    Page<Course> getCourseList = courseRepository.findCoursesByTeacher(pageable, teacher);
    List<Course> courseList = getCourseList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList, null), getCourseList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> filterCourse(Long userId, Pageable pageable, String search) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    CourseSpecificationsBuilder builder = new CourseSpecificationsBuilder();
    Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
    Matcher matcher = pattern.matcher(search + ",");
    while (matcher.find()) {
      builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
    }

    Specification<Course> spec = builder.build();

    Page<Course> coursePage = courseRepository.findAll(spec, pageable);
    List<Course> courseList = coursePage.getContent();
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", toCourseResponseDTOList(courseList, user), coursePage.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getCourseByCategory(Pageable pageable, String type, int point) {
    if (point == 0) {

    }
    return null;
  }

  private List<CourseResponseDTO> toCourseResponseDTOList(List<Course> courseList, User user){
    List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();
    for (Course course : courseList){
      CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(course);

      if (user != null) {
        Optional<LikeCourse> likeCourse = likeCourseRepository.findLikeCourseByCourseAndUser(course, user);

        if (likeCourse.isPresent()) {
          courseResponseDTO.setLike(true);
        }
      }

      //Get discount now of course
      Optional<DiscountDetail> discountDetail = discountDetailRepository.findDiscountByDayInPeriod(course.getId(), new Date());
      if (discountDetail.isPresent()) {
        DiscountResponseDTO discountResponseDTO = DiscountMapper.INSTANCE.discountToDiscountResponseDTO(discountDetail.get().getDiscount());
        courseResponseDTO.setDiscountResponseDTO(discountResponseDTO);
      }
      courseResponseDTOList.add(courseResponseDTO);
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

  @Override
  public ResponseEntity<?> getTypeCourse() {
    List<String> type = courseRepository.getType();
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List course!", type));
  }
}

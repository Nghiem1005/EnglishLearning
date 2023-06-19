package com.example.english.service.impl;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Course;
import com.example.english.entities.Feedback;
import com.example.english.entities.StudentCourse;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.FeedbackMapper;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.FeedbackRepository;
import com.example.english.repository.StudentCourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.FeedbackService;
import com.example.english.service.StorageService;
import com.example.english.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
  @Autowired private CourseRepository courseRepository;
  @Autowired
  private FeedbackRepository feedbackRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private StorageService storageService;

  @Override
  public ResponseEntity<?> getAllFeedback(Pageable pageable) {
    Page<Feedback> getFeedbackList = feedbackRepository.findAll(pageable);
    List<Feedback> feedbackList = getFeedbackList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", toListFeedbackResponseDTO(feedbackList),
        getFeedbackList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> getAllFeedbackByCourse(Pageable pageable, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Page<Feedback> getFeedbackList = feedbackRepository.findFeedbacksByCourse(pageable, course);
    List<Feedback> feedbackList = getFeedbackList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", toListFeedbackResponseDTO(feedbackList),
        getFeedbackList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getAllFeedbackByCourseAndPending(Pageable pageable, Long courseId,
      boolean pending) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    Page<Feedback> getFeedbackList = feedbackRepository.findFeedbacksByCourseAndPending(pageable, course, pending);
    List<Feedback> feedbackList = getFeedbackList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", toListFeedbackResponseDTO(feedbackList),
        getFeedbackList.getTotalPages()));
  }

  private List<DiscussResponseDTO> toListFeedbackResponseDTO(List<Feedback> feedbackList){
    //Convert feedback to feedback response dto
    List<DiscussResponseDTO> feedbackResponseDTOList = new ArrayList<>();
    for (Feedback feedback : feedbackList) {
      DiscussResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback);
      feedbackResponseDTOList.add(feedbackResponseDTO);
    }

    return feedbackResponseDTOList;
  }

  @Override
  public ResponseEntity<ResponseObject> createFeedback(DiscussRequestDTO feedbackRequestDTO)
      throws IOException {
    Feedback feedback = FeedbackMapper.INSTANCE.feedbackRequestDTOToFeedback(feedbackRequestDTO);

    User student = userRepository.findById(feedbackRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + feedbackRequestDTO.getStudentId()));
    Course course = courseRepository.findById(feedbackRequestDTO.getSubjectId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + feedbackRequestDTO.getSubjectId()));
    Optional<StudentCourse> getStudentCourse = studentCourseRepository.findStudentCourseByCourseAndUser(course, student);

    //Check if the student has taken this course or not
    if (!getStudentCourse.isPresent()){
      throw new ResourceNotFoundException("Students who have never taken this course ");
    }

    feedback.setCourse(course);
    feedback.setUser(student);

    if (feedbackRequestDTO.getImages() != null){
      List<String> nameFiles = Utils.storeFile(feedbackRequestDTO.getImages());
      feedback.setImages(nameFiles);
    }

    Feedback feedbackSaved = feedbackRepository.save(feedback);
    DiscussResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedbackSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create feedback successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateFeedback(String content, Long id) {
    Feedback feedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    feedback.setId(id);
    feedback.setContent(content);
    Feedback feedbackSaved = feedbackRepository.save(feedback);
    DiscussResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedbackSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update feedback successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteFeedback(Long id) {
    Feedback getFeedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));

    feedbackRepository.delete(getFeedback);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete feedback successfully!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getFeedbackById(Long id) {
    Feedback feedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback with ID = " + id));
    DiscussResponseDTO feedbackResponseDTO = FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get feedback.", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<?> getFeedbackByFeedbackMain(Pageable pageable, Long feedbackMain) {
    Feedback getFeedbackMain = feedbackRepository.findById(feedbackMain)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find feedback main with ID = " + feedbackMain));

    Page<Feedback> getFeedbackList = feedbackRepository.findFeedbacksByMainFeedback(pageable, getFeedbackMain);
    List<Feedback> feedbackList = getFeedbackList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", toListFeedbackResponseDTO(feedbackList),
        getFeedbackList.getTotalPages()));
  }

}

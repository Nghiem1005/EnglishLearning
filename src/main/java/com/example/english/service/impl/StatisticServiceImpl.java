package com.example.english.service.impl;

import com.example.english.dto.response.CourseResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.StatisticByDayResponseDTO;
import com.example.english.dto.response.StatisticInfoCourseResponseDTO;
import com.example.english.dto.response.StatisticResponseDTO;
import com.example.english.entities.Course;
import com.example.english.entities.Lesson;
import com.example.english.entities.User;
import com.example.english.entities.enums.Role;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.CoursesMapper;
import com.example.english.models.ICourseSeller;
import com.example.english.models.IStatisticDay;
import com.example.english.repository.BillRepository;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.FeedbackRepository;
import com.example.english.repository.LessonRepository;
import com.example.english.repository.LikeCourseRepository;
import com.example.english.repository.StudentCourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.StatisticService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private FeedbackRepository feedbackRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private LikeCourseRepository likeCourseRepository;
  @Autowired private BillRepository billRepository;
  @Autowired private UserRepository userRepository;
  @Override
  public ResponseEntity<?> statisticInfoCourse(Long courseId) {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    StatisticInfoCourseResponseDTO info = new StatisticInfoCourseResponseDTO();
    info.setDocumentAmount(course.getDocument().size());
    info.setStudentAmount(studentCourseRepository.findStudentCoursesByCourse(course).size());
    info.setFeedbackAmount(feedbackRepository.findFeedbacksByCourse(course).size());
    info.setLikeAmount(likeCourseRepository.findLikeCoursesByCourse(course).size());
    info.setLessonAmount(lessonRepository.findLessonsByCourse(course).size());
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Info", info));
  }

  @Override
  public ResponseEntity<?> statisticBestSeller() {
    List<ICourseSeller> courseSellerList = billRepository.bestSeller();
    List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();
    for (ICourseSeller iCourseSeller : courseSellerList) {
      Course course = courseRepository.findById(iCourseSeller.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + iCourseSeller.getCourseId()));
      CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(course);
      courseResponseDTOList.add(courseResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Best seller.", courseResponseDTOList));
  }

  @Override
  public ResponseEntity<?> generalStatistics() {
    List<User> userList = userRepository.findUsersByRole(Role.STUDENT);
    List<Course> courseList = courseRepository.findAll();
    List<User> teacherList = userRepository.findUsersByRole(Role.TEACHER);
    double revenue = 0;
    if (billRepository.totalPrice().isPresent()) {
      revenue = billRepository.totalPrice().get();
    }


    StatisticResponseDTO statisticResponseDTO = new StatisticResponseDTO(userList.size(), revenue, courseList.size(), teacherList.size());
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "General statistic.", statisticResponseDTO));
  }

  @Override
  public ResponseEntity<?> statisticsByDay() {
    //Get revenue in 7 days
    List<IStatisticDay> revenueStatistic = billRepository.findRevenueByDay(7);
    //Get student attended course in 7 days
    List<IStatisticDay> statisticStudentCourseVideoList = studentCourseRepository.findAllStudentTypeByDay(7);
    //Get new member in 7 days
    List<IStatisticDay> newMemberStatistic = userRepository.findAllNewMemberByDay(7);

    String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    List<StatisticByDayResponseDTO> statisticByDayResponseDTOS = new ArrayList<>();

    //Get get list of last 7 days from recent to more distant
    List<String> dayList = new ArrayList<>();
    for (int i=0; i < 7; i++){
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_WEEK, - i);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek == 1){
        dayList.add("Sun");
      } else {
        dayList.add(daysOfWeek[dayOfWeek - 2]);
      }
    }

    //Set statistics by corresponding date
    for (String day : dayList){
      StatisticByDayResponseDTO statisticByDayResponseDTO = new StatisticByDayResponseDTO();
      statisticByDayResponseDTO.setNewMember(0);
      statisticByDayResponseDTO.setRevenueTotal(0);
      statisticByDayResponseDTO.setStudentCourse(0);

      statisticByDayResponseDTO.setTime(day);

      //Set amount new member
      for (IStatisticDay statistic : newMemberStatistic) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setNewMember((int) statistic.getTotalValue());
        }
      }

      //Set amount student attended course
      for (IStatisticDay statistic : statisticStudentCourseVideoList) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setStudentCourse((int) statistic.getTotalValue());
        }
      }

      //Set revenue
      for (IStatisticDay statistic : revenueStatistic) {
        if (daysOfWeek[statistic.getWeekDay()].equals(day)){
          statisticByDayResponseDTO.setRevenueTotal(statistic.getTotalValue());
        }
      }

      statisticByDayResponseDTOS.add(statisticByDayResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Statistic by day.", statisticByDayResponseDTOS));
  }
}

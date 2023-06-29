package com.example.english.service.impl;

import com.example.english.dto.request.PaymentRequestDTO;
import com.example.english.dto.response.BillResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Bill;
import com.example.english.entities.Course;
import com.example.english.entities.Discount;
import com.example.english.entities.StudentCourse;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.BillMapper;
import com.example.english.repository.BillRepository;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.DiscountRepository;
import com.example.english.repository.StudentCourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.PaymentService;
import com.mservice.config.Environment;
import com.mservice.enums.RequestType;
import com.mservice.models.PaymentResponse;
import com.mservice.processor.CreateOrderMoMo;
import com.mservice.shared.utils.LogUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Autowired private BillRepository billRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private DiscountRepository discountRepository;

  @Override
  public ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception{
    LogUtils.init();
    Environment environment = Environment.selectEnv("dev");
    String requestId = String.valueOf(System.currentTimeMillis());
    PaymentResponse responseObject = CreateOrderMoMo.process(environment, requestId, requestId,
        String.valueOf(paymentRequestDTO.getPrice().longValue()), paymentRequestDTO.getDescription(), returnUrl, returnUrl,
        paymentRequestDTO.getStudentId().toString() + "-" + paymentRequestDTO.getCourseId(), RequestType.CAPTURE_WALLET, true);
    return ResponseEntity.status(HttpStatus.OK).body(responseObject);
  }

  @Override
  public ResponseEntity<?> saveBill(String extraData, String amount, Integer resultCode) {
    String message = "Payment success!";
    HttpStatus httpStatus = HttpStatus.OK;

    //Check process payment
    if (extraData.equals("") || resultCode != 0){
      message = "Payment fail!";
      httpStatus = HttpStatus.BAD_REQUEST;
    } else {
      String[] str = extraData.split("-");
      Long studentId = Long.parseLong(str[0]);
      Long courseId = Long.parseLong(str[1]);
      //Create bill momo
      Course course = courseRepository.findById(courseId)
          .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

      User student = userRepository.findById(studentId)
          .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

      Bill bill = new Bill();
      bill.setPaymentMethod("MOMO");
      bill.setCourse(course);
      bill.setUser(student);

      //Calculator price
      Optional<Discount> discount = discountRepository.findDiscountByCourseAndCreateDateBeforeAndEndDateAfter(course, new Date(), new Date());
      if (discount.isPresent()) {
        BigDecimal price = course.getPrice().multiply (
            BigDecimal.valueOf((100 - discount.get().getPercent()) / 100));
        bill.setPrice(price);
      } else {
        bill.setPrice(course.getPrice());
      }

      Bill billSaved = billRepository.save(bill);

      //Attend student in course
      StudentCourse studentCourse = new StudentCourse();
      studentCourse.setCourse(billSaved.getCourse());
      studentCourse.setUser(billSaved.getUser());
      studentCourse.setProgress(1);
      studentCourseRepository.save(studentCourse);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(httpStatus, message));
  }
}

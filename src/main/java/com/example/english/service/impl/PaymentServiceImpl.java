package com.example.english.service.impl;

import com.example.english.config.VNPayConfig;
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
/*import com.mservice.config.Environment;
import com.mservice.enums.RequestType;
import com.mservice.models.PaymentResponse;
import com.mservice.processor.CreateOrderMoMo;
import com.mservice.shared.utils.LogUtils;*/
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
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

  /*@Override
  public ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception{
    LogUtils.init();
    Environment environment = Environment.selectEnv("dev");
    String requestId = String.valueOf(System.currentTimeMillis());
    PaymentResponse responseObject = CreateOrderMoMo.process(environment, requestId, requestId,
        String.valueOf(paymentRequestDTO.getPrice()), paymentRequestDTO.getDescription(), returnUrl, returnUrl,
        paymentRequestDTO.getStudentId().toString() + "-" + paymentRequestDTO.getCourseId(), RequestType.CAPTURE_WALLET, true);
    return ResponseEntity.status(HttpStatus.OK).body(responseObject);
  }*/

  @Override
  public ResponseEntity<?> createPaymentVNPay(PaymentRequestDTO paymentRequestDTO,
      String returnUrl) {
    //Create bill momo
    Course course = courseRepository.findById(paymentRequestDTO.getCourseId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + paymentRequestDTO.getCourseId()));
    User student = userRepository.findById(paymentRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + paymentRequestDTO.getStudentId()));

    //Create vn pay
    String vnp_Version = "2.1.0";
    String vnp_Command = "pay";
//        String vnp_TxnRef = String.valueOf(orderId);
    String vnp_TxnRef = paymentRequestDTO.getCourseId() + "f" + paymentRequestDTO.getStudentId() + "f" + VNPayConfig.getRandomNumber(7);
    String vnp_IpAddr = "127.0.0.1";
    String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

    Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_RequestId", vnp_RequestId);
    vnp_Params.put("vnp_Version", vnp_Version);
    vnp_Params.put("vnp_Command", vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(paymentRequestDTO.getPrice() * 100));
    vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("vnp_BankCode", "NCP");
    vnp_Params.put("vnp_ReturnUrl", returnUrl);
    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);


    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    vnp_Params.put("vnp_OrderInfo", "Thanh toán khóa học: " + course.getName());
    vnp_Params.put("vnp_Locale", "vn");
    vnp_Params.put("vnp_OrderType", "other");


//        urlReturn += VNPayConfig.vnp_Returnurl;
//        vnp_Params.put("vnp_ReturnUrl", urlReturn);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    List fieldNames = new ArrayList(vnp_Params.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator itr = fieldNames.iterator();
    while (itr.hasNext()) {
      String fieldName = (String) itr.next();
      String fieldValue = (String) vnp_Params.get(fieldName);
      if ((fieldValue != null) && (fieldValue.length() > 0)) {
        //Build hash data
        hashData.append(fieldName);
        hashData.append('=');
        try {
          hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
          //Build query
          query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
          query.append('=');
          query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        if (itr.hasNext()) {
          query.append('&');
          hashData.append('&');
        }
      }
    }
    String queryUrl = query.toString();
    String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create vn pay success", paymentUrl));
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
      bill.setPaymentMethod("VNPay");
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

  @Override
  public ResponseEntity<?> saveBillPaymentVNPay(HttpServletRequest request) {
    String message = "Payment success!";
    HttpStatus httpStatus = HttpStatus.OK;
    Map fields = new HashMap();
    for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
      String fieldName = null;
      String fieldValue = null;
      try {
        fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
        fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      if ((fieldValue != null) && (fieldValue.length() > 0)) {
        fields.put(fieldName, fieldValue);
      }
    }

    String vnp_SecureHash = request.getParameter("vnp_SecureHash");
    if (fields.containsKey("vnp_SecureHashType")) {
      fields.remove("vnp_SecureHashType");
    }
    if (fields.containsKey("vnp_SecureHash")) {
      fields.remove("vnp_SecureHash");
    }
    String signValue = VNPayConfig.hashAllFields(fields);
    if (signValue.equals(vnp_SecureHash)) {
      if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
        String TxnRef = request.getParameter("vnp_TxnRef");
        Long courseId = Long.valueOf(TxnRef.split("f")[0]);
        Long studentId = Long.valueOf(TxnRef.split("f")[1]);

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

        Bill bill = new Bill();
        bill.setPaymentMethod("VNPAY");
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
      } else {
        message = "Payment fail!";
        httpStatus = HttpStatus.BAD_REQUEST;
      }
    } else {
      message = "Payment fail!";
      httpStatus = HttpStatus.BAD_REQUEST;
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(httpStatus, message));
  }
}

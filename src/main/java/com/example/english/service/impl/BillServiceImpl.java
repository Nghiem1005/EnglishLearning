package com.example.english.service.impl;

import com.example.english.dto.request.BillRequestDTO;
import com.example.english.dto.response.BillResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Bill;
import com.example.english.entities.Course;
import com.example.english.entities.Discount;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.BillMapper;
import com.example.english.repository.BillRepository;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.DiscountRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.BillService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {
  @Autowired private BillRepository billRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private DiscountRepository discountRepository;

  @Override
  public ResponseEntity<?> saveBill(Long studentId, Long courseId,
      BillRequestDTO billRequestDTO) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    Bill bill = new Bill();
    bill.setPaymentMethod(billRequestDTO.getPaymentMethod());
    bill.setCourse(course);
    bill.setUser(student);

    //Calculator price
    Optional<Discount> discount = discountRepository.findDiscountByCourse(course);
    if (discount.isPresent()) {
      BigDecimal price = course.getPrice().multiply (
          BigDecimal.valueOf((100 - discount.get().getPercent()) / 100));
      bill.setPrice(price);
    } else {
      bill.setPrice(course.getPrice());
    }

    BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(billRepository.save(bill));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create bill success!", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateBill(Long billId, BillRequestDTO billRequestDTO) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));

    bill.setPaymentMethod(billRequestDTO.getPaymentMethod());

    BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(billRepository.save(bill));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create bill success!", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteBill(Long billId) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));

    billRepository.delete(bill);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete bill success!"));
  }

  @Override
  public ResponseEntity<ResponseObject> getAllBill(Pageable pageable) {
    Page<Bill> getBillList = billRepository.findAll(pageable);
    List<Bill> billList = getBillList.getContent();
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);
      billResponseDTOList.add(billResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTOList, getBillList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> getBillById(Long billId) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> getBillByStudent(Long studentId) {
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find student with ID = " + studentId));

    List<Bill> billList = billRepository.findBillsByUser(student);
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);
      billResponseDTOList.add(billResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTOList));
  }

  @Override
  public ResponseEntity<ResponseObject> getBillByCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    List<Bill> billList = billRepository.findBillsByCourse(course);
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    for (Bill bill : billList){
      BillResponseDTO billResponseDTO = BillMapper.INSTANCE.billToBillResponseDTO(bill);
      billResponseDTOList.add(billResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List bill", billResponseDTOList));
  }
}

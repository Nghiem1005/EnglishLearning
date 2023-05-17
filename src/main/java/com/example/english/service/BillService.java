package com.example.english.service;

import com.example.english.dto.request.BillRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BillService {
  ResponseEntity<?> saveBill(Long studentId, Long courseId, BillRequestDTO billRequestDTO);
  ResponseEntity<?> updateBill(Long billId, BillRequestDTO billRequestDTO);
  ResponseEntity<?> deleteBill(Long billId);
  ResponseEntity<?> getAllBill(Pageable pageable);
  ResponseEntity<?> getBillById(Long billId);
  ResponseEntity<?> getBillByStudent(Long studentId);
  ResponseEntity<?> getBillByCourse(Long courseId);
}

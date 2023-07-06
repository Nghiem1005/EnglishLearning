package com.example.english.service;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.request.WordRequestDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DiscountService {
  ResponseEntity<?> createDiscount(DiscountRequestDTO discountRequestDTO);
  //ResponseEntity<?> addCourseDiscount(Long discountId, List<Long> listCourse);
  ResponseEntity<?> updateDiscount(Long discountId, DiscountRequestDTO discountRequestDTO);
  ResponseEntity<?> getAllDiscount(Pageable pageable);
  ResponseEntity<?> getDiscountById(Long id);
  ResponseEntity<?> deleteDiscount(Long id);
}

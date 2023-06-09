package com.example.english.service;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.request.WordRequestDTO;
import java.io.IOException;
import org.springframework.http.ResponseEntity;

public interface DiscountService {
  ResponseEntity<?> createDiscount(Long courseId, DiscountRequestDTO discountRequestDTO);
  ResponseEntity<?> updateDiscount(Long discountId, DiscountRequestDTO discountRequestDTO);
  ResponseEntity<?> deleteDiscount(Long id);
}

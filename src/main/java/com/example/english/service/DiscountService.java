package com.example.english.service;

import com.example.english.dto.request.DiscountRequestDTO;
import org.springframework.http.ResponseEntity;

public interface DiscountService {
  ResponseEntity<?> createDiscount(Long courseId, DiscountRequestDTO discountRequestDTO);
}

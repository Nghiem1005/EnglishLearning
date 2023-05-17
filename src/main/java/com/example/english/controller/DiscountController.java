package com.example.english.controller;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/discount")
public class DiscountController {
  @Autowired private DiscountService discountService;

  @PostMapping(value = "")
  public ResponseEntity<?> createDiscount(@RequestParam(name = "courseId") Long courseId,@RequestBody DiscountRequestDTO discountRequestDTO) {
    return discountService.createDiscount(courseId, discountRequestDTO);
  }
}

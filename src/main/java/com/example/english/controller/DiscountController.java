package com.example.english.controller;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.request.WordRequestDTO;
import com.example.english.service.DiscountService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/discount")
public class DiscountController {
  @Autowired private DiscountService discountService;

  @PostMapping(value = "")
  public ResponseEntity<?> createDiscount(@RequestParam(name = "courseId") Long courseId, @RequestBody DiscountRequestDTO discountRequestDTO) {
    List<Integer> ints = new ArrayList<>();

    return discountService.createDiscount(courseId, discountRequestDTO);
  }

  @PutMapping
  public ResponseEntity<?> updateDiscount(@RequestParam(name = "discountId") Long discountId, @RequestBody DiscountRequestDTO discountRequestDTO) {
    return discountService.updateDiscount(discountId, discountRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteDiscount(@RequestParam(name = "id") Long id) {

    return discountService.deleteDiscount(id);
  }
}

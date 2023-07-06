package com.example.english.controller;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.request.WordRequestDTO;
import com.example.english.service.DiscountService;
import com.example.english.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<?> createDiscount(@RequestBody DiscountRequestDTO discountRequestDTO) {
    return discountService.createDiscount(discountRequestDTO);
  }

  @PutMapping
  public ResponseEntity<?> updateDiscount(@RequestParam(name = "discountId") Long discountId, @RequestBody DiscountRequestDTO discountRequestDTO) {
    return discountService.updateDiscount(discountId, discountRequestDTO);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteDiscount(@RequestParam(name = "id") Long id) {
    return discountService.deleteDiscount(id);
  }

  /*@PostMapping(value = "/course")
  public ResponseEntity<?> addCourseDiscount(@io.swagger.v3.oas.annotations.parameters.RequestBody List<Long> listCourse, @RequestParam(name = "discountId") Long discountId) {
    return discountService.addCourseDiscount(discountId, listCourse);
  }*/

  @GetMapping
  public ResponseEntity<?> getAllDiscount(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return discountService.getAllDiscount(pageable);
  }

  @GetMapping(value = "/day")
  public ResponseEntity<?> getAllDiscountInStartAndEndDate(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
      @RequestParam(name = "startDate") Date startDate, @RequestParam(name = "endDate") Date endDate) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return discountService.getDiscountInStartAndEndDay(startDate, endDate, pageable);
  }

  @GetMapping(value = {"/{id}"})
  public ResponseEntity<?> getDiscountById(@PathVariable(name = "id") Long id) {
    return discountService.getDiscountById(id);
  }
}

package com.example.english.service.impl;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.response.DiscountResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Course;
import com.example.english.entities.Discount;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.CoursesMapper;
import com.example.english.mapper.DiscountMapper;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.DiscountRepository;
import com.example.english.service.DiscountService;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private DiscountRepository discountRepository;

  @Override
  public ResponseEntity<?> createDiscount(Long courseId, DiscountRequestDTO discountRequestDTO) {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

    if (discountRequestDTO.getPercent() > 100 || discountRequestDTO.getPercent() < 0) {
      throw new BadRequestException("Discount percent must between 0 and 100");
    }

    Discount discount = DiscountMapper.INSTANCE.discountRequestDTOToDiscount(discountRequestDTO);

    discount.setCode(UUID.randomUUID().toString());
    discount.setCourse(course);

    Optional<Discount> discountByDay = discountRepository.findDiscountByDayInPeriod(new Date(), discountRequestDTO.getEndDate());
    if (discountByDay.isPresent()) {
      throw new BadRequestException("Discount of this course have already other discount");
    }

    discount.setEndDate(discountRequestDTO.getEndDate());

    DiscountResponseDTO discountResponseDTO = DiscountMapper.INSTANCE.discountToDiscountResponseDTO(discountRepository.save(discount));
    discountResponseDTO.setCourseResponseDTO(CoursesMapper.INSTANCE.courseToCourseResponseDTO(course));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create discount success", discountResponseDTO));
  }
}

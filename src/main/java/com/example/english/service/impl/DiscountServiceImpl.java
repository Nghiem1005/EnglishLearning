package com.example.english.service.impl;

import com.example.english.dto.request.DiscountRequestDTO;
import com.example.english.dto.response.CourseResponseDTO;
import com.example.english.dto.response.DiscountResponseDTO;
import com.example.english.dto.response.LessonResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.WordResponseDTO;
import com.example.english.entities.Course;
import com.example.english.entities.Discount;
import com.example.english.entities.DiscountDetail;
import com.example.english.entities.Lesson;
import com.example.english.entities.Word;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.CoursesMapper;
import com.example.english.mapper.DiscountMapper;
import com.example.english.mapper.LessonMapper;
import com.example.english.mapper.WordMapper;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.DiscountDetailRepository;
import com.example.english.repository.DiscountRepository;
import com.example.english.service.DiscountService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
  @Autowired private CourseRepository courseRepository;
  @Autowired private DiscountRepository discountRepository;
  @Autowired private DiscountDetailRepository discountDetailRepository;

  @Override
  public ResponseEntity<?> createDiscount(DiscountRequestDTO discountRequestDTO) {
    if (discountRequestDTO.getPercent() > 100 || discountRequestDTO.getPercent() < 0) {
      throw new BadRequestException("Discount percent must between 0 and 100");
    }

    Discount discount = DiscountMapper.INSTANCE.discountRequestDTOToDiscount(discountRequestDTO);

    discount.setCode(UUID.randomUUID().toString());

    discount.setEndDate(discountRequestDTO.getEndDate());
    discount.setStartDate(discountRequestDTO.getStartDate());
    Discount discountSaved = discountRepository.save(discount);
    DiscountResponseDTO discountResponseDTO = DiscountMapper.INSTANCE.discountToDiscountResponseDTO(discountSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create discount success", discountResponseDTO));
  }

  public void addCourseDiscount(Discount discount, List<Long> listCourse) {

    List<DiscountDetail> discountDetailList = new ArrayList<>();
    for (Long courseId : listCourse) {
      Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + courseId));

      List<Discount> discountList = discountRepository.findDiscountByDayInPeriod(discount.getStartDate(), discount.getEndDate());
      for (Discount getDiscount : discountList) {
        Optional<DiscountDetail> discountDetail = discountDetailRepository.findDiscountDetailByCourseAndDiscount(course, getDiscount);
        if (discountDetail.isPresent()) {
          throw new BadRequestException("This course have discount by id: " + getDiscount.getId());
        }
      }

      DiscountDetail discountDetail = new DiscountDetail();
      discountDetail.setDiscount(discount);
      discountDetail.setCourse(course);

      discountDetailList.add(discountDetail);
    }

    discountDetailRepository.saveAll(discountDetailList);

    /*List<DiscountResponseDTO> discountResponseDTOS = new ArrayList<>();
    for (DiscountDetail discountDetail : discountDetailListSaved) {
      DiscountResponseDTO discountResponseDTO = DiscountMapper.INSTANCE.discountToDiscountResponseDTO(discountDetail.getDiscount());
      CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(discountDetail.getCourse());
      List<CourseResponseDTO> courseResponseDTOS = new ArrayList<>();
      courseResponseDTOS.add(courseResponseDTO);
      discountResponseDTO.setCourseResponseDTOS(courseResponseDTOS);

      discountResponseDTOS.add(discountResponseDTO);
    }
    return discountResponseDTOS;*/
  }

  @Override
  public ResponseEntity<?> updateDiscount(Long discountId, DiscountRequestDTO discountRequestDTO) {
    Discount discount = discountRepository.findById(discountId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

    if (discountRequestDTO.getContent() != null) {
      discount.setContent(discountRequestDTO.getContent());
    }

    if (discountRequestDTO.getPercent() > 0) {
      discount.setPercent(discountRequestDTO.getPercent());
    }

    if (discountRequestDTO.getEndDate() != null) {
      discount.setEndDate(discountRequestDTO.getEndDate());
    }

    if (!discountRequestDTO.getCourseId().isEmpty()) {
      addCourseDiscount(discount, discountRequestDTO.getCourseId());
    }
    Discount discountSaved = discountRepository.save(discount);

    DiscountResponseDTO discountResponseDTO = convertDiscountToDiscountResponse(discountSaved);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update word success", discountResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllDiscount(Pageable pageable) {
    Page<Discount> discountPage = discountRepository.findAll(pageable);
    List<Discount> discountList = discountPage.getContent();
    List<DiscountResponseDTO> discountResponseDTOS = new ArrayList<>();
    for (Discount discount : discountList) {
      discountResponseDTOS.add(convertDiscountToDiscountResponse(discount));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List discount.", discountResponseDTOS,
        discountPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getDiscountById(Long id) {
    Discount getDiscount = discountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + id));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List discount.", convertDiscountToDiscountResponse(getDiscount)));
  }

  @Override
  public ResponseEntity<?> getDiscountInStartAndEndDay(Date startDate, Date endDate, Pageable pageable) {
    Page<Discount> discountPage = discountRepository.findDiscountsByStartDateAndEndDate(startDate, endDate, pageable);
    List<Discount> discountList = discountPage.getContent();
    List<DiscountResponseDTO> discountResponseDTOS = new ArrayList<>();
    for (Discount discount : discountList) {
      discountResponseDTOS.add(convertDiscountToDiscountResponse(discount));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List discount.", discountResponseDTOS,
        discountPage.getTotalPages()));
  }

  private DiscountResponseDTO convertDiscountToDiscountResponse(Discount discount) {
    DiscountResponseDTO discountResponseDTO = DiscountMapper.INSTANCE.discountToDiscountResponseDTO(discount);
    discountResponseDTO.setStartDate(discount.getStartDate());
    discountResponseDTO.setEndDate(discount.getEndDate());

    List<DiscountDetail> discountDetailList = discountDetailRepository.findDiscountDetailsByDiscount(discount);
    List<CourseResponseDTO> courseResponseDTOS = new ArrayList<>();
    for (DiscountDetail discountDetail : discountDetailList) {
      CourseResponseDTO courseResponseDTO = CoursesMapper.INSTANCE.courseToCourseResponseDTO(discountDetail.getCourse());
      courseResponseDTOS.add(courseResponseDTO);
    }

    discountResponseDTO.setCourseResponseDTOS(courseResponseDTOS);
    return discountResponseDTO;
  }

  @Override
  public ResponseEntity<?> deleteDiscount(Long id) {
    Discount getDiscount = discountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + id));

    discountRepository.delete(getDiscount);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete discount successfully!"));
  }
}

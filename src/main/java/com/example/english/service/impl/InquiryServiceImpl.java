package com.example.english.service.impl;

import com.example.english.dto.request.DiscussRequestDTO;
import com.example.english.dto.response.DiscussResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Inquiry;
import com.example.english.entities.Lesson;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.InquiryMapper;
import com.example.english.repository.InquiryRepository;
import com.example.english.repository.LessonRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.InquiryService;
import com.example.english.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InquiryServiceImpl implements InquiryService {
  @Autowired private LessonRepository lessonRepository;
  @Autowired private InquiryRepository inquiryRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public ResponseEntity<?> getAllFeedback(Pageable pageable) {
    return null;
  }

  @Override
  public ResponseEntity<?> getAllInquiryByLesson(Pageable pageable, Long lessonId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + lessonId));

    Page<Inquiry> getInquiryList = inquiryRepository.findInquiriesByLesson(pageable, lesson);
    List<Inquiry> inquiryList = getInquiryList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", toListInquiryResponseDTO(inquiryList),
        getInquiryList.getTotalPages()));
  }

  private List<DiscussResponseDTO> toListInquiryResponseDTO(List<Inquiry> inquiryList){
    //Convert feedback to feedback response dto
    List<DiscussResponseDTO> inquiryResponseDTOList = new ArrayList<>();
    for (Inquiry inquiry : inquiryList) {
      DiscussResponseDTO inquiryResponseDTO = InquiryMapper.INSTANCE.inquiryToInquiryResponseDTO(inquiry);
      if (inquiry.getMainInquiry() != null) {
        inquiryResponseDTO.setMainDiscuss(InquiryMapper.INSTANCE.inquiryToInquiryResponseDTO(inquiry.getMainInquiry()));
      }
      inquiryResponseDTOList.add(inquiryResponseDTO);
    }

    return inquiryResponseDTOList;
  }

  @Override
  public ResponseEntity<?> createInquiry(DiscussRequestDTO inquiryRequestDTO) throws IOException {
    Inquiry inquiry = InquiryMapper.INSTANCE.inquiryRequestDTOToInquiry(inquiryRequestDTO);

    User student = userRepository.findById(inquiryRequestDTO.getStudentId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + inquiryRequestDTO.getStudentId()));
    Lesson lesson = lessonRepository.findById(inquiryRequestDTO.getSubjectId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find course with ID = " + inquiryRequestDTO.getSubjectId()));

    inquiry.setLesson(lesson);
    inquiry.setUser(student);

    if (inquiryRequestDTO.getImages() != null){
      List<String> nameFiles = Utils.storeFile(inquiryRequestDTO.getImages());
      inquiry.setImages(nameFiles);
    }

    //Get main inquiry
    if (inquiryRequestDTO.getMainDiscuss() != null) {
      Inquiry inquiryMain = inquiryRepository.findById(inquiryRequestDTO.getMainDiscuss())
          .orElseThrow(() -> new ResourceNotFoundException("Could not find inquiry with ID = " + inquiryRequestDTO.getMainDiscuss()));
      inquiry.setMainInquiry(inquiryMain);
    }


    Inquiry inquirySaved = inquiryRepository.save(inquiry);
    DiscussResponseDTO inquiryResponseDTO = InquiryMapper.INSTANCE.inquiryToInquiryResponseDTO(inquirySaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create inquiry successfully!", inquiryResponseDTO));
  }

  @Override
  public ResponseEntity<?> updateInquiry(String content, Long id) {
    Inquiry inquiry = inquiryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find inquiry with ID = " + id));
    inquiry.setId(id);
    inquiry.setContent(content);
    Inquiry inquirySaved = inquiryRepository.save(inquiry);
    DiscussResponseDTO feedbackResponseDTO = InquiryMapper.INSTANCE.inquiryToInquiryResponseDTO(inquirySaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update inquiry successfully!", feedbackResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteInquiry(Long id) {
    Inquiry getInquiry = inquiryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find inquiry with ID = " + id));

    inquiryRepository.delete(getInquiry);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete inquiry successfully!"));
  }

  @Override
  public ResponseEntity<?> getInquiryById(Long id) {
    Inquiry inquiry = inquiryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find inquiry with ID = " + id));
    DiscussResponseDTO inquiryResponseDTO = InquiryMapper.INSTANCE.inquiryToInquiryResponseDTO(inquiry);
    if (inquiry.getMainInquiry() != null) {
      inquiryResponseDTO.setMainDiscuss(InquiryMapper.INSTANCE.inquiryToInquiryResponseDTO(inquiry.getMainInquiry()));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get inquiry.", inquiryResponseDTO));
  }

  @Override
  public ResponseEntity<?> getInquiryByInquiryMain(Pageable pageable, Long inquiryMain) {
    Inquiry getInquiryMain = inquiryRepository.findById(inquiryMain)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find inquiry main with ID = " + inquiryMain));

    Page<Inquiry> getInquiryList = inquiryRepository.findInquiriesByMainInquiry(pageable, getInquiryMain);
    List<Inquiry> inquiryList = getInquiryList.getContent();

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "List feedback.", toListInquiryResponseDTO(inquiryList),
        getInquiryList.getTotalPages()));
  }
}

package com.example.english.service.impl;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.response.ExamResponseDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Exam;
import com.example.english.entities.Lesson;
import com.example.english.entities.Part;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.ExamMapper;
import com.example.english.mapper.PartMapper;
import com.example.english.repository.ExamRepository;
import com.example.english.repository.PartRepository;
import com.example.english.service.PartService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl implements PartService {
  @Autowired private ExamRepository examRepository;
  @Autowired private PartRepository partRepository;

  @Override
  public ResponseEntity<?> createPart(Long examId, PartRequestDTO partRequestDTO) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    PartResponseDTO partResponseDTO = new PartResponseDTO();
    try {
      Part part = PartMapper.INSTANCE.partRequestDTOToPart(partRequestDTO);
      part.setExam(exam);

      List<Part> partList = partRepository.findPartsByExam(exam);
      part.setSerial(partList.size() + 1);

      Part partSaved = partRepository.save(part);

      partResponseDTO = PartMapper.INSTANCE.partToPartResponseDTO(partSaved);
    } catch (Exception e) {
      examRepository.delete(exam);
      throw new BadRequestException(e.getMessage());
    }


    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create part success", partResponseDTO));
  }

  @Override
  public ResponseEntity<?> updatePart(Long partId, PracticeRequestDTO practiceRequestDTO) {
    return null;
  }

  @Override
  public ResponseEntity<?> deletePart(Long partId) {
    return null;
  }
}

package com.example.english.service.impl;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.PracticeResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Part;
import com.example.english.entities.Practice;
import com.example.english.entities.PracticeDetail;
import com.example.english.entities.User;
import com.example.english.entities.enums.PartType;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.PartMapper;
import com.example.english.mapper.PracticeMapper;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.PartRepository;
import com.example.english.repository.PracticeDetailRepository;
import com.example.english.repository.PracticeRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.PracticeService;
import com.example.english.service.StorageService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PracticeServiceImpl implements PracticeService {
  @Autowired private PracticeRepository practiceRepository;
  @Autowired private StorageService storageService;
  @Autowired private PartRepository partRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PracticeDetailRepository practiceDetailRepository;
  @Override
  public ResponseEntity<?> createPractice(Long userId, PracticeRequestDTO practiceRequestDTO) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Practice practice = PracticeMapper.INSTANCE.practiceRequestDTOToPractice(practiceRequestDTO);

    //Add parts to practice
    List<PartResponseDTO> partResponseDTOS = new ArrayList<>();
    for (Long partId: practiceRequestDTO.getPart()) {
      Part part = partRepository.findById(partId).orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partId));

      //Create practice detail
      PracticeDetail practiceDetail = new PracticeDetail();
      practiceDetail.setPart(part);
      practiceDetail.setPractice(practice);
      practiceDetailRepository.save(practiceDetail);

      //Return part response
      PartResponseDTO partResponseDTO = PartMapper.INSTANCE.partToPartResponseDTO(part);
      partResponseDTOS.add(partResponseDTO);
    }

    Practice practiceSaved = practiceRepository.save(practice);
    PracticeResponseDTO practiceResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResponseDTO(practiceSaved);
    practiceResponseDTO.setPartResponseDTOS(partResponseDTOS);
    practiceResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(user));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create practice success", practiceResponseDTO));
  }

  @Override
  public ResponseEntity<?> updatePractice(Long practiceId, PracticeRequestDTO practiceRequestDTO) {
    Practice practice = practiceRepository.findById(practiceId).orElseThrow(() -> new ResourceNotFoundException("Could not find practice with ID = " + practiceId));

    //Set fields of practice
    if (practiceRequestDTO.getName() != null) {
      practice.setName(practiceRequestDTO.getName());
    }

    if (practiceRequestDTO.getPeriod() != practice.getPeriod()) {
      practice.setPeriod(practiceRequestDTO.getPeriod());
    }

    /*if (practiceRequestDTO.getResult() != null) {
      practice.setResult(practiceRequestDTO.getResult());
    }*/

    PracticeResponseDTO practiceResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResponseDTO(practiceRepository.save(practice));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update result practice success", practiceResponseDTO));
  }
}

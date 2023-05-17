package com.example.english.service.impl;

import com.example.english.dto.request.ExerciseRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.response.ContestResponseDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.PracticeResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Contest;
import com.example.english.entities.ContestDetail;
import com.example.english.entities.Part;
import com.example.english.entities.Practice;
import com.example.english.entities.PracticeDetail;
import com.example.english.entities.User;
import com.example.english.entities.enums.PartType;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.ContestMapper;
import com.example.english.mapper.PartMapper;
import com.example.english.mapper.PracticeMapper;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.PartRepository;
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
  @Override
  public ResponseEntity<?> createPractice(Long userId, PracticeRequestDTO practiceRequestDTO) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Practice practice = PracticeMapper.INSTANCE.practiceRequestDTOToPractice(practiceRequestDTO);

    Practice practiceSaved = practiceRepository.save(practice);

    List<PartResponseDTO> partResponseDTOS = new ArrayList<>();
    try{
      //Create contest detail
      for (ExerciseRequestDTO requestDTO : practiceRequestDTO.getExerciseRequestDTOS()) {
        //Create part
        Part part = new Part();
        part.setDescription(requestDTO.getDescription());

        part.setType(PartType.valueOf(requestDTO.getType()));

        if (requestDTO.getDocument() != null){
          part.setDocument(storageService.uploadFile(requestDTO.getDocument()));
        }

        Part partSaved = partRepository.save(part);

        List<QuestionResponseDTO> questionResponseDTOS = QuestionServiceImpl.createQuestion(requestDTO.getQuestionRequestDTOS(), partSaved);

        PartResponseDTO partResponseDTO = PartMapper.INSTANCE.partToPartResponseDTO(partSaved);
        partResponseDTO.setQuestionResponseDTOS(questionResponseDTOS);
        partResponseDTO.setSerial(requestDTO.getSerial());

        partResponseDTOS.add(partResponseDTO);

        //Create practice detail
        PracticeDetail practiceDetail = new PracticeDetail();
        practiceDetail.setPart(partSaved);
        practiceDetail.setSerial(requestDTO.getSerial());
        practiceDetail.setPractice(practice);
      }
    } catch (Exception e) {
      practiceRepository.delete(practice);
      throw new BadRequestException(e.getMessage());
    }

    PracticeResponseDTO practiceResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResponseDTO(practice);
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

    if (practiceRequestDTO.getResult() != null) {
      practice.setResult(practiceRequestDTO.getResult());
    }

    PracticeResponseDTO practiceResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResponseDTO(practiceRepository.save(practice));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update result practice success", practiceResponseDTO));
  }
}

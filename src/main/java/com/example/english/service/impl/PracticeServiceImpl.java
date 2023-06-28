package com.example.english.service.impl;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.request.PartResultRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.request.ResultRequestDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.PartResultResponseDTO;
import com.example.english.dto.response.PracticeResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.ResultResponseDTO;
import com.example.english.entities.Answer;
import com.example.english.entities.Part;
import com.example.english.entities.Practice;
import com.example.english.entities.PracticeDetail;
import com.example.english.entities.Question;
import com.example.english.entities.Result;
import com.example.english.entities.User;
import com.example.english.entities.enums.PartType;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.PartMapper;
import com.example.english.mapper.PracticeMapper;
import com.example.english.mapper.ResultMapper;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.PartRepository;
import com.example.english.repository.PracticeDetailRepository;
import com.example.english.repository.PracticeRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.repository.ResultRepository;
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
  @Autowired private QuestionRepository questionRepository;
  @Autowired private AnswerRepository answerRepository;
  @Autowired private ResultRepository resultRepository;
  @Override
  public ResponseEntity<?> createPractice(Long userId, PracticeRequestDTO practiceRequestDTO) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Practice practice = PracticeMapper.INSTANCE.practiceRequestDTOToPractice(practiceRequestDTO);
    Practice practiceSaved = practiceRepository.save(practice);

    //Add parts to practice
    int point = 0;
    int totalQuestion = 0;
    List<PartResultResponseDTO> partResultResponseDTOS = new ArrayList<>();
    for (PartResultRequestDTO partResultRequestDTO: practiceRequestDTO.getPartResultRequestDTOS()) {
      Part part = partRepository.findById(partResultRequestDTO.getPartId())
          .orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partResultRequestDTO.getPartId()));

      //Create practice detail
      PracticeDetail practiceDetail = new PracticeDetail();
      practiceDetail.setPart(part);
      practiceDetail.setPractice(practice);
      practiceDetailRepository.save(practiceDetail);

      //Save result practice
      List<ResultResponseDTO> resultResponseDTOS = new ArrayList<>();
      for (ResultRequestDTO resultRequestDTO : partResultRequestDTO.getResultRequestDTOS()) {
        Question question = questionRepository.findById(resultRequestDTO.getQuestionId())
            .orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + resultRequestDTO.getQuestionId()));

        Result result = new Result();
        result.setQuestion(question);
        result.setPractice(practiceSaved);
        result.setChoice(resultRequestDTO.getChoice());

        //Get answer true
        Answer answer = answerRepository.findAnswerByQuestionAndCorrectIsTrue(question);

        if (resultRequestDTO.getChoice() == answer.getSerial()) {
          result.setCorrect(true);
          point++;
        } else {
          result.setCorrect(false);
        }

        totalQuestion++;

        Result resultSaved = resultRepository.save(result);

        ResultResponseDTO resultResponseDTO = ResultMapper.INSTANCE.resultToResultResponseDTO(resultSaved);
        resultResponseDTO.setAnswer(answer.getSerial());

        resultResponseDTOS.add(resultResponseDTO);
      }

      //Return part result response
      PartResultResponseDTO partResultResponseDTO = new PartResultResponseDTO();
      partResultResponseDTO.setResultResponseDTOS(resultResponseDTOS);
      partResultResponseDTO.setSerial(partResultResponseDTO.getSerial());
      partResultResponseDTO.setPartId(part.getId());
      partResultResponseDTOS.add(partResultResponseDTO);
    }
    PracticeResponseDTO practiceResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResponseDTO(practiceSaved);
    practiceResponseDTO.setPartResultResponseDTOS(partResultResponseDTOS);
    practiceResponseDTO.setResult(point + "/" + totalQuestion);
    practiceResponseDTO.setUserResponseDTO(UsersMapper.MAPPER.userToUserResponseDTO(user));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create practice success", practiceResponseDTO));
  }

  @Override
  public ResponseEntity<?> updatePractice(Long practiceId, PracticeRequestDTO practiceRequestDTO) {
    Practice practice = practiceRepository.findById(practiceId).orElseThrow(() -> new ResourceNotFoundException("Could not find practice with ID = " + practiceId));

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

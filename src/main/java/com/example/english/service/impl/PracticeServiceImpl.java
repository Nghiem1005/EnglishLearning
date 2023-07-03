package com.example.english.service.impl;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.request.PartResultRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.request.ResultRequestDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.PartResultResponseDTO;
import com.example.english.dto.response.PracticeResponseDTO;
import com.example.english.dto.response.PracticeResultResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.ResultResponseDTO;
import com.example.english.entities.Answer;
import com.example.english.entities.Part;
import com.example.english.entities.Practice;
import com.example.english.entities.PracticeDetail;
import com.example.english.entities.Question;
import com.example.english.entities.QuestionPhrase;
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
import com.example.english.repository.QuestionPhraseRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.repository.ResultRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.PracticeService;
import com.example.english.service.StorageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PracticeServiceImpl implements PracticeService {
  @Autowired private PracticeRepository practiceRepository;
  @Autowired private QuestionPhraseRepository questionPhraseRepository;
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
    practice.setUser(user);
    Practice practiceSaved = practiceRepository.save(practice);

    //Add parts to practice
    int point = 0;
    int totalQuestion = 0;
    List<PartResultResponseDTO> partResultResponseDTOS = new ArrayList<>();
    try {
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
          Optional<Answer> getAnswer = answerRepository.findAnswerByQuestionAndCorrectIsTrue(question);
          if (getAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Question have id is " + question.getId() + "not have answer true");
          }
          Answer answer = getAnswer.get();

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
    } catch (Exception e) {
      practiceRepository.delete(practiceSaved);
      throw new BadRequestException(e.getMessage());
    }
    practiceSaved.setResult(point + "/" + totalQuestion);
    PracticeResponseDTO practiceResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResponseDTO(practiceRepository.save(practiceSaved));
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

  @Override
  public ResponseEntity<?> getPracticeResultByUser(Long userId, Pageable pageable) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Page<Practice> practices = practiceRepository.findPracticesByUser(user, pageable);
    if (practices.isEmpty()) {
      throw new ResourceNotFoundException("User have not practice");
    }
    List<Practice> practiceList = practices.getContent();

    List<PracticeResultResponseDTO> practiceResultResponseDTOS = new ArrayList<>();
    for (Practice practice : practiceList) {
      PracticeResultResponseDTO practiceResultResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResultResponseDTO(practice);

      //Get exam
      List<PracticeDetail> practiceDetailList = practiceDetailRepository.findPracticeDetailsByPractice(practice);
      practiceResultResponseDTO.setExamId(practiceDetailList.get(0).getPart().getExam().getId());
      practiceResultResponseDTO.setExamName(practiceDetailList.get(0).getPart().getExam().getName());

      //Get part result
      List<PartResultResponseDTO> partResultResponseDTOS = new ArrayList<>();
      for (PracticeDetail practiceDetail : practiceDetailList) {
        PartResultResponseDTO partResultResponseDTO = new PartResultResponseDTO();
        partResultResponseDTO.setPartId(practiceDetail.getPart().getId());
        partResultResponseDTO.setSerial(practiceDetail.getPart().getSerial());

        partResultResponseDTOS.add(partResultResponseDTO);
      }

      practiceResultResponseDTO.setPartResultResponseDTOS(partResultResponseDTOS);
      practiceResultResponseDTOS.add(practiceResultResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get result practice by user success", practiceResultResponseDTOS, practices.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getPracticeResultByPractice(Long practiceId) {
    Practice practice = practiceRepository.findById(practiceId).orElseThrow(() -> new ResourceNotFoundException("Could not find practice with ID = " + practiceId));

    PracticeResultResponseDTO practiceResultResponseDTO = PracticeMapper.INSTANCE.practiceToPracticeResultResponseDTO(practice);

    //Get exam
    List<PracticeDetail> practiceDetailList = practiceDetailRepository.findPracticeDetailsByPractice(practice);
    practiceResultResponseDTO.setExamId(practiceDetailList.get(0).getPart().getExam().getId());
    practiceResultResponseDTO.setExamName(practiceDetailList.get(0).getPart().getExam().getName());

    //Get part result
    List<PartResultResponseDTO> partResultResponseDTOS = new ArrayList<>();
    for (PracticeDetail practiceDetail : practiceDetailList) {
      PartResultResponseDTO partResultResponseDTO = new PartResultResponseDTO();
      partResultResponseDTO.setPartId(practiceDetail.getPart().getId());
      partResultResponseDTO.setSerial(practiceDetail.getPart().getSerial());

      //Get result question
      List<ResultResponseDTO> resultResponseDTOS = new ArrayList<>();
      List<QuestionPhrase> questionPhraseList = questionPhraseRepository.findQuestionPhrasesByPart(practiceDetail.getPart());
      for (QuestionPhrase questionPhrase : questionPhraseList) {
        List<Question> questionList = questionRepository.findQuestionsByQuestionPhrase(questionPhrase);
        for (Question question : questionList) {
          Result result = resultRepository.findResultByPracticeAndQuestion(practice, question);

          ResultResponseDTO resultResponseDTO = ResultMapper.INSTANCE.resultToResultResponseDTO(result);

          //Get answer
          Optional<Answer> getAnswer = answerRepository.findAnswerByQuestionAndCorrectIsTrue(question);
          if (getAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Question have id is " + question.getId() + "not have answer true");
          }
          resultResponseDTO.setAnswer(getAnswer.get().getSerial());

          resultResponseDTOS.add(resultResponseDTO);
        }
      }
      partResultResponseDTO.setResultResponseDTOS(resultResponseDTOS);
      partResultResponseDTOS.add(partResultResponseDTO);
    }

    practiceResultResponseDTO.setPartResultResponseDTOS(partResultResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get result practice by practice success", practiceResultResponseDTO));
  }
}

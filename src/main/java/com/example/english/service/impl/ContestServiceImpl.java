package com.example.english.service.impl;

import com.example.english.dto.request.ContestRequestDTO;
import com.example.english.dto.request.ExerciseRequestDTO;
import com.example.english.dto.request.PartResultRequestDTO;
import com.example.english.dto.request.QuestionResponse;
import com.example.english.dto.request.ResultRequestDTO;
import com.example.english.dto.response.ContestResponseDTO;
import com.example.english.dto.response.ContestResultResponseDTO;
import com.example.english.dto.response.ExamResponseDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.PartResultResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.ResultResponseDTO;
import com.example.english.entities.Answer;
import com.example.english.entities.Contest;
import com.example.english.entities.ContestDetail;
import com.example.english.entities.ContestResult;
import com.example.english.entities.Exam;
import com.example.english.entities.Part;
import com.example.english.entities.Question;
import com.example.english.entities.Result;
import com.example.english.entities.User;
import com.example.english.entities.enums.PartType;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.ContestMapper;
import com.example.english.mapper.PartMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.ContestDetailRepository;
import com.example.english.repository.ContestRepository;
import com.example.english.repository.ContestResultRepository;
import com.example.english.repository.PartRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.ContestService;
import com.example.english.service.StorageService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContestServiceImpl implements ContestService {
  @Autowired private ContestRepository contestRepository;
  @Autowired private StorageService storageService;
  @Autowired private PartRepository partRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ContestResultRepository contestResultRepository;
  @Autowired private ContestDetailRepository contestDetailRepository;
  @Autowired private QuestionRepository questionRepository;
  @Autowired private AnswerRepository answerRepository;
  @Override
  public ResponseEntity<?> createContest(ContestRequestDTO contestRequestDTO) {
    Contest contest = ContestMapper.INSTANCE.contestRequestDTOToContest(contestRequestDTO);

    Contest contestSaved = contestRepository.save(contest);

    List<PartResponseDTO> partResponseDTOS = new ArrayList<>();
    try{
      //Create contest detail
      for (ExerciseRequestDTO requestDTO : contestRequestDTO.getExerciseRequestDTOS()) {
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

        //Create contest detail
        ContestDetail contestDetail = new ContestDetail();
        contestDetail.setPart(partSaved);
        contestDetail.setSerial(requestDTO.getSerial());
        contestDetail.setContest(contest);
        contestDetailRepository.save(contestDetail);

      }
    } catch (Exception e) {
      contestRepository.delete(contest);
      throw new BadRequestException(e.getMessage());
    }

    ContestResponseDTO contestResponseDTO = ContestMapper.INSTANCE.contestToContestResponseDTO(contest);
    contestResponseDTO.setPartResponseDTOS(partResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create contest success", contestResponseDTO));
  }

  @Override
  public ResponseEntity<?> updateResult(Long contestId, Long userId, float point) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
    Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new ResourceNotFoundException("Could not find contest with ID = " + contestId));

    ContestResult contestResult = new ContestResult();
    contestResult.setContest(contest);
    contestResult.setUser(user);
    if (point > 10 || point < 0) {
      throw new BadRequestException("Point must between 0 and 10");
    }
    contestResult.setPoint(point);
    ContestResult contestResultSaved = contestResultRepository.save(contestResult);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update point contest success", contestResult));
  }

  @Override
  public ResponseEntity<?> markContest(Long contestId, Long userId,
      List<PartResultRequestDTO> partResultRequestDTOS) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
    Contest contest = contestRepository.findById(contestId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find contest with ID = " + contestId));

    List<PartResultResponseDTO> partResultResponseDTOS = new ArrayList<>();

    int point = 0;

    //Check and save result of each part of contest
    for (PartResultRequestDTO partResultRequestDTO : partResultRequestDTOS) {
      Part part = partRepository.findById(partResultRequestDTO.getPartId())
          .orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partResultRequestDTO.getPartId()));

      ContestDetail contestDetail = contestDetailRepository.findContestDetailByContestAndPart(contest, part)
          .orElseThrow(() -> new ResourceNotFoundException("Could not find contest detail with part ID = " + part.getId()));

      //Check and save result of each question of part
      List<ResultResponseDTO> resultResponseDTOS = new ArrayList<>();
      for (ResultRequestDTO resultRequestDTO : partResultRequestDTO.getResultRequestDTOS()) {
        Question question = questionRepository.findById(resultRequestDTO.getQuestionId())
            .orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + resultRequestDTO.getQuestionId()));

        Answer answer = answerRepository.findAnswerByQuestionAndCorrectIsTrue(question);

        //Save result
        Result result = new Result();
        result.setQuestion(question);
        result.setUser(user);
        result.setChoice(resultRequestDTO.getChoice());
        if (answer.getSerial() == resultRequestDTO.getChoice()) {
          point++;
          result.setCorrect(true);
        } else {
          result.setCorrect(false);
        }

        //Create result response
        ResultResponseDTO resultResponseDTO = new ResultResponseDTO();
        resultResponseDTO.setChoice(resultResponseDTO.getChoice());
        resultResponseDTO.setCorrect(result.isCorrect());
        resultResponseDTO.setQuestionId(question.getId());
        resultResponseDTO.setAnswer(answer.getSerial());

        resultResponseDTOS.add(resultResponseDTO);
      }

      //Create result of part response
      PartResultResponseDTO partResultResponseDTO = new PartResultResponseDTO();
      partResultResponseDTO.setPartId(part.getId());
      partResultResponseDTO.setSerial(contestDetail.getSerial());
      partResultResponseDTO.setResultResponseDTOS(resultResponseDTOS);
    }

    ContestResultResponseDTO contestResultResponseDTO = new ContestResultResponseDTO();
    contestResultResponseDTO.setContestId(contest.getId());
    contestResultResponseDTO.setUserId(user.getId());
    contestResultResponseDTO.setPartResultResponseDTOS(partResultResponseDTOS);
    contestResultResponseDTO.setPoint(point);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Result of contest", contestResultResponseDTO));
  }

  @Override
  public ResponseEntity<?> getContestById(Long contestId) {
    Contest contest = contestRepository.findById(contestId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find contest with ID = " + contestId));

    List<PartResponseDTO> partResponseDTOS = new ArrayList<>();

    //Get parts of contest
    List<ContestDetail> contestDetails = contestDetailRepository.findContestDetailsByContest(contest);
    for (ContestDetail contestDetail : contestDetails) {

      //Get question response
      List<Question> questions = questionRepository.findQuestionsByPart(contestDetail.getPart());
      List<QuestionResponseDTO> questionResponseDTOS = QuestionServiceImpl.convertQuestionToQuestionResponse(
          questions);

      PartResponseDTO partResponseDTO = PartMapper.INSTANCE.partToPartResponseDTO(contestDetail.getPart());
      partResponseDTO.setQuestionResponseDTOS(questionResponseDTOS);
      partResponseDTO.setSerial(contestDetail.getSerial());

      partResponseDTOS.add(partResponseDTO);
    }

    ContestResponseDTO contestResponseDTO = ContestMapper.INSTANCE.contestToContestResponseDTO(
        contest);
    contestResponseDTO.setPartResponseDTOS(partResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get contest by ID", contestResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteContest(Long id) {
    Contest getContest = contestRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find contest with ID = " + id));

    contestRepository.delete(getContest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete contest successfully!"));
  }

  public List<Question> getQuestionByContest(Contest contest) {
    List<Question> questions = new ArrayList<>();

    List<ContestDetail> contestDetails = contestDetailRepository.findContestDetailsByContest(contest);

    for (ContestDetail contestDetail : contestDetails) {
      List<Question> questionList = questionRepository.findQuestionsByPart(contestDetail.getPart());
      questions.addAll(questionList);
    }
    return questions;
  }
}

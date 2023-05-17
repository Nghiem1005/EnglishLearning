package com.example.english.service.impl;

import com.example.english.dto.request.QuestionRequestDTO;
import com.example.english.dto.response.AnswerResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Answer;
import com.example.english.entities.Part;
import com.example.english.entities.Question;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.QuestionMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.PartRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.service.QuestionService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {
  @Autowired private PartRepository partRepository;
  @Autowired private static StorageService storageService;
  @Autowired private static QuestionRepository questionRepository;
  @Autowired private static AnswerRepository answerRepository;

  @Override
  public ResponseEntity<?> addQuestion(Long partId, List<QuestionRequestDTO> questionRequestDTOS)
      throws IOException {
    Part part = partRepository.findById(partId).orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partId));

    List<QuestionResponseDTO> questionList = createQuestion(questionRequestDTOS, part);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add question success", questionList));
  }

  public static List<QuestionResponseDTO> createQuestion(List<QuestionRequestDTO> questionRequestDTOList, Part part)
      throws IOException {
    List<QuestionResponseDTO> questionList = new ArrayList<>();
    for (QuestionRequestDTO questionRequestDTO : questionRequestDTOList) {
      Question question = new Question();
      question.setContent(questionRequestDTO.getContent());
      question.setSerial(questionRequestDTO.getSerial());

      if (questionRequestDTO.getImage() != null) {
        question.setImage(storageService.uploadFile(questionRequestDTO.getImage()));
      }

      question.setPart(part);

      Question questionSaved = questionRepository.save(question);

      AnswerServiceImpl.createAnswer(questionRequestDTO.getAnswerRequestDTOS(), questionSaved);

      questionList.add(QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionSaved));
    }
    return questionList;
  }

  public static List<QuestionResponseDTO> convertQuestionToQuestionResponse(List<Question> questions) {
    List<QuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
    for (Question question : questions) {
      QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(question);

      //Get answer response
      List<Answer> answers = answerRepository.findAnswersByQuestion(question);
      List<AnswerResponseDTO> answerResponseDTOS = AnswerServiceImpl.convertAnswerToAnswerResponse(answers);
      questionResponseDTO.setAnswerResponseDTOS(answerResponseDTOS);

      questionResponseDTOS.add(questionResponseDTO);
    }
    return questionResponseDTOS;
  }

}

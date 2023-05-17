package com.example.english.service.impl;

import com.example.english.dto.request.AnswerRequestDTO;
import com.example.english.dto.response.AnswerResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Answer;
import com.example.english.entities.Question;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.AnswerMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.service.AnswerService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {
  @Autowired private static AnswerRepository answerRepository;
  @Autowired private QuestionRepository questionRepository;

  @Override
  public ResponseEntity<?> addAnswer(Long questionId, List<AnswerRequestDTO> answerRequestDTOS) {
    Question  question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + questionId));

    List<AnswerResponseDTO> answerList = createAnswer(answerRequestDTOS, question);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add answer success", answerList));
  }

  public static List<AnswerResponseDTO> createAnswer(List<AnswerRequestDTO> answerRequestDTOList, Question question) {
    List<AnswerResponseDTO> answerList = new ArrayList<>();

    for (AnswerRequestDTO answerRequestDTO : answerRequestDTOList) {
      Answer answer = new Answer();
      answer.setContent(answerRequestDTO.getContent());
      answer.setCorrect(answerRequestDTO.isCorrect());
      answer.setSerial(answerRequestDTO.getSerial());

      answer.setQuestion(question);

      Answer answerSaved = answerRepository.save(answer);

      answerList.add(AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answerSaved));
    }
    return answerList;
  }

  public static List<AnswerResponseDTO> convertAnswerToAnswerResponse(List<Answer> answers) {
    List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
    for (Answer answer : answers) {
      AnswerResponseDTO answerResponseDTO = AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answer);
      answerResponseDTOS.add(answerResponseDTO);
    }
    return answerResponseDTOS;
  }
}

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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {
  @Autowired private AnswerRepository answerRepository;
  @Autowired private QuestionRepository questionRepository;

  @Override
  public ResponseEntity<?> addAnswer(Long questionId, AnswerRequestDTO answerRequestDTO) {

    AnswerResponseDTO answerResponseDTO = createAnswer(questionId, answerRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add answer success", answerResponseDTO));
  }

  @Override
  public AnswerResponseDTO createAnswer(Long questionId, AnswerRequestDTO answerRequestDTO) {
    Question  question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + questionId));

    Answer answer = new Answer();
    answer.setContent(answerRequestDTO.getContent());
    answer.setCorrect(answerRequestDTO.isCorrect());

    List<Answer> answerList = answerRepository.findAnswersByQuestion(question);
    answer.setSerial(answerList.size() + 1);

    answer.setQuestion(question);

    Answer answerSaved = answerRepository.save(answer);

    AnswerResponseDTO answerResponseDTO = AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answerSaved);
    return answerResponseDTO;
  }

  @Override
  public ResponseEntity<ResponseObject> updateAnswer(Long id, AnswerRequestDTO answerRequestDTO) {

    Answer answer = answerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find answer with ID = " + id));

    answer.setContent(answerRequestDTO.getContent());
    answer.setCorrect(answerRequestDTO.isCorrect());

    AnswerResponseDTO answerResponseDTO = AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answerRepository.save(answer));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update answer success!", answerResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteAnswer(Long id) {
    Answer getAnswer = answerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find answer with ID = " + id));

    answerRepository.delete(getAnswer);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete answer successfully!"));
  }

  /*public static List<AnswerResponseDTO> createListAnswer(List<AnswerRequestDTO> answerRequestDTOList, Question question) {
    List<AnswerResponseDTO> answerList = new ArrayList<>();

    for (AnswerRequestDTO answerRequestDTO : answerRequestDTOList) {
      answerList.add(createAnswer(answerRequestDTO, question));
    }
    return answerList;
  }*/

  /*public static AnswerResponseDTO createAnswer(AnswerRequestDTO answerRequestDTO, Question question) {
    Answer answer = new Answer();
    answer.setContent(answerRequestDTO.getContent());
    answer.setCorrect(answerRequestDTO.isCorrect());

    List<Answer> answerList = answerRepository.findAnswersByQuestion(question);
    answer.setSerial(answerList.size() + 1);

    answer.setQuestion(question);

    Answer answerSaved = answerRepository.save(answer);

    AnswerResponseDTO answerResponseDTO = AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answerSaved);
    return answerResponseDTO;
  }*/

  public static List<AnswerResponseDTO> convertAnswerToAnswerResponse(List<Answer> answers) {
    List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
    for (Answer answer : answers) {
      AnswerResponseDTO answerResponseDTO = AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answer);
      answerResponseDTOS.add(answerResponseDTO);
    }
    return answerResponseDTOS;
  }
}

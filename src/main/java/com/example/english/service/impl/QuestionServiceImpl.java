package com.example.english.service.impl;

import com.example.english.dto.request.AnswerRequestDTO;
import com.example.english.dto.request.QuestionRequestDTO;
import com.example.english.dto.response.AnswerResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Answer;
import com.example.english.entities.Part;
import com.example.english.entities.Question;
import com.example.english.entities.QuestionPhrase;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.QuestionMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.PartRepository;
import com.example.english.repository.QuestionPhraseRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.service.AnswerService;
import com.example.english.service.QuestionService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {
  @Autowired private QuestionPhraseRepository questionPhraseRepository;
  @Autowired private StorageService storageService;
  @Autowired private QuestionRepository questionRepository;
  @Autowired private AnswerService answerService;

  @Override
  public ResponseEntity<?> addQuestion(Long questionPhraseId, QuestionRequestDTO questionRequestDTO)
      throws IOException {

    QuestionResponseDTO questionList = createQuestion(questionPhraseId, questionRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add question success", questionList));
  }

  @Override
  public QuestionResponseDTO createQuestion(Long questionPhraseId,
      QuestionRequestDTO questionRequestDTO) throws IOException {
    QuestionPhrase questionPhrase = questionPhraseRepository.findById(questionPhraseId).orElseThrow(() -> new ResourceNotFoundException("Could not find question phrase with ID = " + questionPhraseId));

    Question question = new Question();
    question.setContent(questionRequestDTO.getContent());

    List<Question> questionList = questionRepository.findQuestionsByQuestionPhrase(questionPhrase);
    question.setSerial(questionList.size() + 1);

    question.setQuestionPhrase(questionPhrase);

    Question questionSaved = questionRepository.save(question);

    List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
    for (AnswerRequestDTO answerRequestDTO : questionRequestDTO.getAnswerRequestDTOS()) {
      AnswerResponseDTO answerResponseDTO = answerService.createAnswer(questionSaved.getId(), answerRequestDTO);
      answerResponseDTOS.add(answerResponseDTO);
    }

    QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionSaved);
    questionResponseDTO.setAnswerResponseDTOS(answerResponseDTOS);

    return questionResponseDTO;
  }

  @Override
  public ResponseEntity<ResponseObject> updateQuestion(Long id, QuestionRequestDTO questionRequestDTO) {
    Question question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + id));;

    question.setContent(questionRequestDTO.getContent());

    QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionRepository.save(question));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update question success!", questionResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteQuestion(Long id) {
    Question getQuestion = questionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find question with ID = " + id));

    questionRepository.delete(getQuestion);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete question successfully!"));
  }

  /*public static List<QuestionResponseDTO> createListQuestion(List<QuestionRequestDTO> questionRequestDTOList, QuestionPhrase questionPhrase)
      throws IOException {
    List<QuestionResponseDTO> questionList = new ArrayList<>();
    for (QuestionRequestDTO questionRequestDTO : questionRequestDTOList) {
      QuestionResponseDTO questionResponseDTO = createQuestion(questionRequestDTO, questionPhrase);

      questionList.add(questionResponseDTO);
    }
    return questionList;
  }

  public static QuestionResponseDTO createQuestion(QuestionRequestDTO questionRequestDTO, QuestionPhrase questionPhrase) {

    Question question = new Question();
    question.setContent(questionRequestDTO.getContent());

    List<Question> questionList = questionRepository.findQuestionsByQuestionPhrase(questionPhrase);
    question.setSerial(questionList.size() + 1);

    question.setQuestionPhrase(questionPhrase);

    Question questionSaved = questionRepository.save(question);

    List<AnswerResponseDTO> answerResponseDTOS = AnswerServiceImpl.createListAnswer(questionRequestDTO.getAnswerRequestDTOS(), questionSaved);

    QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionSaved);
    questionResponseDTO.setAnswerResponseDTOS(answerResponseDTOS);

    return questionResponseDTO;
  }*/

  /*public static List<QuestionResponseDTO> convertQuestionToQuestionResponse(List<Question> questions) {
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
  }*/

}

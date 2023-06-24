package com.example.english.service.impl;

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
  @Autowired private static StorageService storageService;
  @Autowired private static QuestionRepository questionRepository;
  @Autowired private static AnswerRepository answerRepository;

  @Override
  public ResponseEntity<?> addQuestion(Long questionPhraseId, QuestionRequestDTO questionRequestDTO) {
    QuestionPhrase questionPhrase = questionPhraseRepository.findById(questionPhraseId).orElseThrow(() -> new ResourceNotFoundException("Could not find question phrase with ID = " + questionPhraseId));

    QuestionResponseDTO questionList = createQuestion(questionRequestDTO, questionPhrase);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add question success", questionList));
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

  public static List<QuestionResponseDTO> createListQuestion(List<QuestionRequestDTO> questionRequestDTOList, QuestionPhrase questionPhrase)
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
    question.setSerial(questionRequestDTO.getSerial());

    question.setQuestionPhrase(questionPhrase);

    Question questionSaved = questionRepository.save(question);

    List<AnswerResponseDTO> answerResponseDTOS = AnswerServiceImpl.createListAnswer(questionRequestDTO.getAnswerRequestDTOS(), questionSaved);

    QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionSaved);
    questionResponseDTO.setAnswerResponseDTOS(answerResponseDTOS);

    return questionResponseDTO;
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

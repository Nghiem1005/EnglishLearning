package com.example.english.service.impl;

import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.request.PracticeRequestDTO;
import com.example.english.dto.response.AnswerResponseDTO;
import com.example.english.dto.response.ExamResponseDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.QuestionPhraseResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Answer;
import com.example.english.entities.Exam;
import com.example.english.entities.Lesson;
import com.example.english.entities.Part;
import com.example.english.entities.PracticeDetail;
import com.example.english.entities.Question;
import com.example.english.entities.QuestionPhrase;
import com.example.english.entities.User;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.AnswerMapper;
import com.example.english.mapper.ExamMapper;
import com.example.english.mapper.PartMapper;
import com.example.english.mapper.QuestionMapper;
import com.example.english.mapper.QuestionPhraseMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.ExamRepository;
import com.example.english.repository.PartRepository;
import com.example.english.repository.QuestionPhraseRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.service.PartService;
import com.example.english.service.QuestionPhraseService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl implements PartService {
  @Autowired private ExamRepository examRepository;
  @Autowired private PartRepository partRepository;
  @Autowired private QuestionPhraseRepository questionPhraseRepository;
  @Autowired private QuestionRepository questionRepository;
  @Autowired private AnswerRepository answerRepository;

  @Override
  public ResponseEntity<?> createPart(Long examId, PartRequestDTO partRequestDTO) {
    Exam exam = examRepository.findById(examId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with ID = " + examId));

    PartResponseDTO partResponseDTO = new PartResponseDTO();
    try {
      Part part = PartMapper.INSTANCE.partRequestDTOToPart(partRequestDTO);
      part.setExam(exam);

      List<Part> partList = partRepository.findPartsByExam(exam);
      //part.setSerial(partList.size() + 1);

      Part partSaved = partRepository.save(part);

      partResponseDTO = PartMapper.INSTANCE.partToPartResponseDTO(partSaved);
    } catch (Exception e) {
      examRepository.delete(exam);
      throw new BadRequestException(e.getMessage());
    }


    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create part success", partResponseDTO));
  }

  @Override
  public ResponseEntity<?> updatePart(Long partId, PracticeRequestDTO practiceRequestDTO) {
    return null;
  }

  @Override
  public ResponseEntity<?> deletePart(Long partId) {
    return null;
  }

  @Override
  public ResponseEntity<?> getPartById(Long partId) {
    Part part = partRepository.findById(partId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partId));

    PartResponseDTO partResponseDTO = PartMapper.INSTANCE.partToPartResponseDTO(part);
    //Get total question
    int totalQuestionPart = 0;
    List<QuestionPhrase> questionPhraseList = questionPhraseRepository.findQuestionPhrasesByPart(part);
    List<QuestionPhraseResponseDTO> questionPhraseResponseDTOS = new ArrayList<>();
    for (QuestionPhrase questionPhrase : questionPhraseList) {
      QuestionPhraseResponseDTO questionPhraseResponseDTO = QuestionPhraseMapper.INSTANCE.questionPhraseToQuestionPhraseResponseDTO(questionPhrase);

      List<Question> questionList = questionRepository.findQuestionsByQuestionPhrase(questionPhrase);
      List<QuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
      for (Question question : questionList) {
        QuestionResponseDTO questionResponseDTO = QuestionMapper.INSTANCE.questionToQuestionResponseDTO(question);

        List<Answer> answerList = answerRepository.findAnswersByQuestion(question);
        List<AnswerResponseDTO> answerResponseDTOS = new ArrayList<>();
        for (Answer answer : answerList) {
          AnswerResponseDTO answerResponseDTO = AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answer);
          answerResponseDTOS.add(answerResponseDTO);
        }
        questionResponseDTO.setAnswerResponseDTOS(answerResponseDTOS);

        questionResponseDTOS.add(questionResponseDTO);
      }
      questionPhraseResponseDTO.setQuestionResponseDTOS(questionResponseDTOS);

      questionPhraseResponseDTOS.add(questionPhraseResponseDTO);
      totalQuestionPart = totalQuestionPart + questionList.size();
    }
    partResponseDTO.setTotalQuestion(totalQuestionPart);
    partResponseDTO.setQuestionPhraseResponseDTOS(questionPhraseResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get part", partResponseDTO));
  }
}

package com.example.english.service.impl;

import com.example.english.dto.request.ExamRequestDTO;
import com.example.english.dto.request.PartRequestDTO;
import com.example.english.dto.response.ExamResponseDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.QuestionPhraseResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Exam;
import com.example.english.entities.Lesson;
import com.example.english.entities.Part;
import com.example.english.entities.enums.PartType;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.ExamMapper;
import com.example.english.mapper.PartMapper;
import com.example.english.repository.AnswerRepository;
import com.example.english.repository.ExamRepository;
import com.example.english.repository.LessonRepository;
import com.example.english.repository.PartRepository;
import com.example.english.repository.QuestionRepository;
import com.example.english.service.ExamService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {
  @Autowired private ExamRepository examRepository;
  @Autowired private LessonRepository lessonRepository;
  @Autowired private QuestionRepository questionRepository;
  @Autowired private AnswerRepository answerRepository;
  @Autowired private PartRepository partRepository;
  @Autowired private StorageService storageService;
  @Override
  public ResponseEntity<?> createExam(ExamRequestDTO examRequestDTO) {
    Exam exam = ExamMapper.INSTANCE.examRequestDTOToExam(examRequestDTO);
    Exam examSaved = examRepository.save(exam);

    ExamResponseDTO examResponseDTO = ExamMapper.INSTANCE.examToExamResponseDTO(examSaved);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create exam success", examResponseDTO));
  }

  @Override
  public ResponseEntity<?> createExamLesson(Long lessonId, ExamRequestDTO examRequestDTO) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Exam exam = ExamMapper.INSTANCE.examRequestDTOToExam(examRequestDTO);
    exam.setLesson(lesson);

    Exam examSaved = examRepository.save(exam);

    ExamResponseDTO examResponseDTO = ExamMapper.INSTANCE.examToExamResponseDTO(examSaved);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create exam lesson success", examResponseDTO));
  }

  private ExamResponseDTO createExamF(ExamRequestDTO examRequestDTO, Exam exam) {
    exam = ExamMapper.INSTANCE.examRequestDTOToExam(examRequestDTO);
    Exam examSaved = examRepository.save(exam);

    ExamResponseDTO examResponseDTO = ExamMapper.INSTANCE.examToExamResponseDTO(examSaved);
    return examResponseDTO;
  }

  @Override
  public ResponseEntity<?> deleteExam(Long examId) {
    Exam exam = examRepository.findById(examId) .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with exam ID = " + examId));

    List<Part> partList = partRepository.findPartsByExam(exam);
    for (Part part : partList) {
      partRepository.delete(part);
    }
    examRepository.delete(exam);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete exam success"));
  }

  /*private List<QuestionResponseDTO> createQuestion(List<QuestionRequestDTO> questionRequestDTOList, Part part)
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

      createAnswer(questionRequestDTO.getAnswerRequestDTOS(), questionSaved);

      questionList.add(QuestionMapper.INSTANCE.questionToQuestionResponseDTO(questionSaved));
    }
    return questionList;
  }*/

  /*private List<AnswerResponseDTO> createAnswer(List<AnswerRequestDTO> answerRequestDTOList, Question question) {
    List<AnswerResponseDTO> answerList = new ArrayList<>();

    for (AnswerRequestDTO answerRequestDTO : answerRequestDTOList) {
      Answer answer = new Answer();
      answer.setContent(answerRequestDTO.getContent());
      answer.setCorrect(answerRequestDTO.isCorrect());

      answer.setQuestion(question);

      Answer answerSaved = answerRepository.save(answer);

      answerList.add(AnswerMapper.INSTANCE.answerToAnswerResponseDTO(answerSaved));
    }
    return answerList;
  }*/
}

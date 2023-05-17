package com.example.english.service.impl;

import com.example.english.dto.request.ExerciseRequestDTO;
import com.example.english.dto.response.ExamResponseDTO;
import com.example.english.dto.response.PartResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Exam;
import com.example.english.entities.Lesson;
import com.example.english.entities.Part;
import com.example.english.entities.enums.PartType;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
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
  public ResponseEntity<?> createExam(Long lessonId, List<ExerciseRequestDTO> examRequestDTOS)
      throws IOException {
    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    List<ExamResponseDTO> examResponseDTOS = new ArrayList<>();
    try{
      //Create exam
      for (ExerciseRequestDTO requestDTO : examRequestDTOS) {
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

        //Create exam
        Exam exam = new Exam();
        exam.setPart(partSaved);
        exam.setLesson(lesson);
        exam.setSerial(requestDTO.getSerial());

        ExamResponseDTO examResponseDTO = new ExamResponseDTO();
        examResponseDTO = examResponseDTO.build(exam);
        examResponseDTO.setPartResponseDTO(partResponseDTO);

        examResponseDTOS.add(examResponseDTO);
      }
    } catch (Exception e) {
      List<Exam> examList = examRepository.findExamsByLesson(lesson);
      for (Exam exam : examList) {
        partRepository.delete(exam.getPart());
        examRepository.delete(exam);
      }
      throw new BadRequestException(e.getMessage());
    }



    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create exam success", examResponseDTOS));
  }

  @Override
  public ResponseEntity<?> deleteExam(Long lessonId, Long partId) {
    Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Could not find lesson with ID = " + lessonId));

    Part part = partRepository.findById(partId).orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partId));

    Exam exam = examRepository.findExamsByLessonAndPart(lesson, part) .orElseThrow(() -> new ResourceNotFoundException("Could not find exam with lesson ID = " + lessonId + " and part ID = " + partId));

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

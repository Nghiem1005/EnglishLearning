package com.example.english.service.impl;

import com.example.english.dto.request.QuestionPhraseRequestDTO;
import com.example.english.dto.request.QuestionRequestDTO;
import com.example.english.dto.response.QuestionPhraseResponseDTO;
import com.example.english.dto.response.QuestionResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.entities.Part;
import com.example.english.entities.Question;
import com.example.english.entities.QuestionPhrase;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.QuestionMapper;
import com.example.english.mapper.QuestionPhraseMapper;
import com.example.english.repository.PartRepository;
import com.example.english.repository.QuestionPhraseRepository;
import com.example.english.service.QuestionPhraseService;
import com.example.english.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuestionPhraseServiceImpl implements QuestionPhraseService {
  @Autowired
  private PartRepository partRepository;

  @Autowired private static QuestionPhraseRepository questionPhraseRepository;

  @Override
  public ResponseEntity<?> createQuestionPhrase(Long partId,
      List<QuestionPhraseRequestDTO> questionPhraseRequestDTOS) throws IOException {
    Part part = partRepository.findById(partId).orElseThrow(() -> new ResourceNotFoundException("Could not find part with ID = " + partId));

    List<QuestionPhraseResponseDTO> questionPhraseResponseDTOS = createQuestionPhrase(questionPhraseRequestDTOS, part);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add question success", questionPhraseResponseDTOS));
  }

  public static List<QuestionPhraseResponseDTO> createQuestionPhrase(List<QuestionPhraseRequestDTO> questionPhraseRequestDTOS, Part part)
      throws IOException {
    List<QuestionPhraseResponseDTO> questionPhraseResponseDTOS = new ArrayList<>();
    for (QuestionPhraseRequestDTO questionPhraseRequestDTO : questionPhraseRequestDTOS) {
      QuestionPhrase questionPhrase = new QuestionPhrase();
      questionPhrase.setSerial(questionPhraseRequestDTO.getSerial());

      if (questionPhraseRequestDTO.getDocument() != null) {
        List<String> nameFiles = Utils.storeFile(questionPhraseRequestDTO.getDocument());
        questionPhrase.setDocument(nameFiles);
      }

      questionPhrase.setPart(part);

      QuestionPhrase questionPhraseSaved = questionPhraseRepository.save(questionPhrase);

      List<QuestionResponseDTO> questionResponseDTOS = QuestionServiceImpl.createListQuestion(questionPhraseRequestDTO.getQuestionRequestDTOS(), questionPhraseSaved);

      QuestionPhraseResponseDTO questionPhraseResponseDTO = QuestionPhraseMapper.INSTANCE.questionPhraseToQuestionPhraseResponseDTO(questionPhraseSaved);
      questionPhraseResponseDTO.setQuestionResponseDTOS(questionResponseDTOS);
      questionPhraseResponseDTOS.add(questionPhraseResponseDTO);
    }
    return questionPhraseResponseDTOS;
  }
}

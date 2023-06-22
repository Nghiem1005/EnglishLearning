package com.example.english.service.impl;

import com.example.english.dto.request.WordRequestDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.WordResponseDTO;
import com.example.english.entities.Answer;
import com.example.english.entities.ListWord;
import com.example.english.entities.Word;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.WordMapper;
import com.example.english.repository.ListWordRepository;
import com.example.english.repository.WordRepository;
import com.example.english.service.StorageService;
import com.example.english.service.WordService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl implements WordService {
  @Autowired private ListWordRepository listWordRepository;
  @Autowired private WordRepository wordRepository;
  @Autowired private StorageService storageService;
  @Override
  public ResponseEntity<?> addWord(Long listWordId, List<WordRequestDTO> wordRequestDTOS)
      throws IOException {
    ListWord listWord = listWordRepository.findById(listWordId).orElseThrow(() -> new ResourceNotFoundException("Could not find list word with ID = " + listWordId));

    List<WordResponseDTO> wordResponseDTOS = new ArrayList<>();

    //Create word
    for (WordRequestDTO wordRequestDTO : wordRequestDTOS) {
      Word word = WordMapper.INSTANCE.wordRequestDTOToWord(wordRequestDTO);

      word.setListWord(listWord);
      if (wordRequestDTO.getImage() != null) {
        word.setImages(storageService.uploadFile(wordRequestDTO.getImage()));
      }
      Word wordSaved = wordRepository.save(word);

      WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(wordSaved);

      wordResponseDTOS.add(wordResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add word to list word success", wordRequestDTOS));
  }

  @Override
  public ResponseEntity<?> updateWord(Long wordId, WordRequestDTO wordRequestDTO)
      throws IOException {
    Word word = wordRepository.findById(wordId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find word with ID = " + wordId));
    if (wordRequestDTO.getContent() != null) {
      word.setContent(wordRequestDTO.getContent());
    }

    if (wordRequestDTO.getDefine() != null) {
      word.setDefine(wordRequestDTO.getDefine());
    }

    if (wordRequestDTO.getExample() != null) {
      word.setDefine(wordRequestDTO.getExample());
    }

    if (wordRequestDTO.getImage() != null) {
      word.setImages(storageService.uploadFile(wordRequestDTO.getImage()));
    }

    Word wordSaved = wordRepository.save(word);

    WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(wordSaved);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update word success", wordResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteWord(Long id) {
    Word getWord = wordRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find word with ID = " + id));

    wordRepository.delete(getWord);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete word successfully!"));
  }
}

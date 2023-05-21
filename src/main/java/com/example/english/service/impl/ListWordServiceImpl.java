package com.example.english.service.impl;

import com.example.english.dto.request.ListWordRequestDTO;
import com.example.english.dto.request.WordRequestDTO;
import com.example.english.dto.response.ListWordResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.WordResponseDTO;
import com.example.english.entities.ListWord;
import com.example.english.entities.User;
import com.example.english.entities.Word;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.ListWordMapper;
import com.example.english.mapper.WordMapper;
import com.example.english.repository.ListWordRepository;
import com.example.english.repository.UserRepository;
import com.example.english.repository.WordRepository;
import com.example.english.service.ListWordService;
import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ListWordServiceImpl implements ListWordService {
  @Autowired private StorageService storageService;
  @Autowired private WordRepository wordRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ListWordRepository listWordRepository;

  @Override
  public ResponseEntity<?> createListWord(Long userId, ListWordRequestDTO listWordRequestDTO)
      throws IOException {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    ListWord listWord = ListWordMapper.INSTANCE.listWordRequestDTOToListWord(listWordRequestDTO);
    listWord.setUser(user);

    ListWord listWordSaved = listWordRepository.save(listWord);

    List<WordResponseDTO> wordResponseDTOS = new ArrayList<>();
    try{
      //Create word of list word
      for (WordRequestDTO wordRequestDTO : listWordRequestDTO.getWordRequestDTOS()) {
        Word word = WordMapper.INSTANCE.wordRequestDTOToWord(wordRequestDTO);

        if (wordRequestDTO.getImage() != null) {
          word.setImages(storageService.uploadFile(wordRequestDTO.getImage()));
        }

        word.setListWord(listWordSaved);
        WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(wordRepository.save(word));
        wordResponseDTOS.add(wordResponseDTO);
      }
    } catch (Exception e) {
      listWordRepository.delete(listWordSaved);
      throw new BadRequestException(e.getMessage());
    }

    ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWordSaved);
    listWordResponseDTO.setWordResponseDTOS(wordResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create list word success", listWordResponseDTO));
  }

  @Override
  public ResponseEntity<?> getListWordById(Long listWordId) {
    ListWord listWord = listWordRepository.findById(listWordId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find list word with ID = " + listWordId));

    ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWord);

    //Get word of list word
    List<WordResponseDTO> wordResponseDTOS = new ArrayList<>();
    List<Word> words = wordRepository.findWordsByListWord(listWord);
    for (Word word : words) {
      WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(word);
      wordResponseDTOS.add(wordResponseDTO);
    }
    listWordResponseDTO.setWordResponseDTOS(wordResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get list word success", listWordResponseDTO));
  }

  /*@Override
  public ResponseEntity<?> addWordToListWord(Long listWordId, ListWordRequestDTO listWordRequestDTO)
      throws IOException {
    ListWord listWord = listWordRepository.findById(listWordId).orElseThrow(() -> new ResourceNotFoundException("Could not find list word with ID = " + listWordId));

    List<WordResponseDTO> wordResponseDTOS = new ArrayList<>();

    //Create word of list word
    for (WordRequestDTO wordRequestDTO : listWordRequestDTO.getWordRequestDTOS()) {
      Word word = WordMapper.INSTANCE.wordRequestDTOToWord(wordRequestDTO);

      if (wordRequestDTO.getImage() != null) {
        word.setImages(storageService.uploadFile(wordRequestDTO.getImage()));
      }

      word.setListWord(listWord);

      WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(wordRepository.save(word));
      wordResponseDTOS.add(wordResponseDTO);
    }

    ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWord);
    listWordResponseDTO.setWordResponseDTOS(wordResponseDTOS);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create list word success", listWordResponseDTO));
  }*/
}

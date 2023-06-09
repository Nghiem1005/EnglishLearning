package com.example.english.service.impl;

import com.example.english.dto.request.ListWordRequestDTO;
import com.example.english.dto.request.WordRequestDTO;
import com.example.english.dto.response.ListWordResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.UserResponseDTO;
import com.example.english.dto.response.WordResponseDTO;
import com.example.english.entities.Blog;
import com.example.english.entities.ListWord;
import com.example.english.entities.User;
import com.example.english.entities.Word;
import com.example.english.exceptions.BadRequestException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.ListWordMapper;
import com.example.english.mapper.UsersMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    if (listWordRequestDTO.getWordRequestDTOS() != null) {

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
    }

    //Get user response
    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(user);

    ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWordSaved);
    listWordResponseDTO.setWordResponseDTOS(wordResponseDTOS);
    listWordResponseDTO.setUserResponseDTO(userResponseDTO);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create list word success", listWordResponseDTO));
  }

  @Override
  public ResponseEntity<?> updateListWord(Long listWordId, ListWordRequestDTO listWordRequestDTO) {
    ListWord listWord = listWordRepository.findById(listWordId).orElseThrow(() -> new ResourceNotFoundException("Could not find list word with ID = " + listWordId));

    if (listWordRequestDTO.getName() != null) {
      listWord.setName(listWordRequestDTO.getName());
    }

    if (listWordRequestDTO.getDescription() != null) {
      listWord.setDescription(listWordRequestDTO.getDescription());
    }
    ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWordRepository.save(listWord));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create list word success", listWordResponseDTO));
  }

  @Override
  public ResponseEntity<?> getListWordById(Pageable pageable, Long listWordId) {
    ListWord listWord = listWordRepository.findById(listWordId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find list word with ID = " + listWordId));

    ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWord);

    //Get word of list word
    List<WordResponseDTO> wordResponseDTOS = new ArrayList<>();
    Page<Word> wordPage = wordRepository.findWordsByListWord(pageable, listWord);
    List<Word> words = wordPage.getContent();
    for (Word word : words) {
      WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(word);
      wordResponseDTOS.add(wordResponseDTO);
    }
    listWordResponseDTO.setWordResponseDTOS(wordResponseDTOS);

    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(listWord.getUser());
    listWordResponseDTO.setUserResponseDTO(userResponseDTO);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get list word success", listWordResponseDTO, wordPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getListWordByUser(Pageable pageable, Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
    Page<ListWord> listWordPage = listWordRepository.findListWordsByUser(pageable, user);
    List<ListWord> listWords = listWordPage.getContent();
    List<ListWordResponseDTO> listWordResponseDTOS = new ArrayList<>();
    for (ListWord listWord : listWords) {
      ListWordResponseDTO listWordResponseDTO = ListWordMapper.INSTANCE.listWordDTOToListWordResponse(listWord);

      UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(user);
      listWordResponseDTO.setUserResponseDTO(userResponseDTO);

      //Get word of list word
      List<WordResponseDTO> wordResponseDTOS = new ArrayList<>();
      Page<Word> wordPage = wordRepository.findWordsByListWord(pageable, listWord);
      List<Word> words = wordPage.getContent();
      for (Word word : words) {
        WordResponseDTO wordResponseDTO = WordMapper.INSTANCE.wordToWordResponseDTO(word);
        wordResponseDTOS.add(wordResponseDTO);
      }
      listWordResponseDTO.setWordResponseDTOS(wordResponseDTOS);

      listWordResponseDTOS.add(listWordResponseDTO);
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get list word success", listWordResponseDTOS, listWordPage.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> deleteListWord(Long listWordId) {
    ListWord listWord = listWordRepository.findById(listWordId).orElseThrow(() -> new ResourceNotFoundException("Could not find list word with ID = " + listWordId));

    listWordRepository.delete(listWord);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete list word success!"));
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

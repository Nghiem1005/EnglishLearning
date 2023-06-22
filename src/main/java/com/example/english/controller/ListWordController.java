package com.example.english.controller;

import com.example.english.dto.request.ListWordRequestDTO;
import com.example.english.service.ListWordService;
import com.example.english.utils.Utils;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/word_list")
public class ListWordController {
  @Autowired private ListWordService listWordService;
  @PostMapping(value = "")
  public ResponseEntity<?> createListWord(@RequestParam(name = "userId") Long userId,
      @ModelAttribute ListWordRequestDTO listWordRequestDTO) throws IOException {
    return listWordService.createListWord(userId, listWordRequestDTO);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<?> getListWordById(@PathVariable(name = "id") Long id,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return listWordService.getListWordById(pageable, id);
  }

  @GetMapping(value = "/user")
  public ResponseEntity<?> getListWordByUser(@RequestParam(name = "userId") Long userId,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return listWordService.getListWordByUser(pageable, userId);
  }

  /*@PostMapping(value = "/word")
  public ResponseEntity<?> addWordToListWord(@RequestParam(name = "listWordId") Long listWordId, @ModelAttribute ListWordRequestDTO listWordRequestDTO)
      throws IOException {
    return listWordService.addWordToListWord(listWordId, listWordRequestDTO);
  }*/
}

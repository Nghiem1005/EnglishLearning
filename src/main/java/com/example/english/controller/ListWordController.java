package com.example.english.controller;

import com.example.english.dto.request.ListWordRequestDTO;
import com.example.english.service.ListWordService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<?> getListWordById(@PathVariable(name = "id") Long id) {
    return listWordService.getListWordById(id);
  }

  /*@PostMapping(value = "/word")
  public ResponseEntity<?> addWordToListWord(@RequestParam(name = "listWordId") Long listWordId, @ModelAttribute ListWordRequestDTO listWordRequestDTO)
      throws IOException {
    return listWordService.addWordToListWord(listWordId, listWordRequestDTO);
  }*/
}

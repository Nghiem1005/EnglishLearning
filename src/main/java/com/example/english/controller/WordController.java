package com.example.english.controller;

import com.example.english.dto.request.WordRequestDTO;
import com.example.english.service.WordService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/word")
public class WordController {
  @Autowired private WordService wordService;
  @PostMapping(value = "")
  public ResponseEntity<?> addWord(@RequestParam(name = "listWordId") Long listWordId, @ModelAttribute
      List<WordRequestDTO> wordRequestDTOS) throws IOException {
    return wordService.addWord(listWordId, wordRequestDTOS);
  }
}

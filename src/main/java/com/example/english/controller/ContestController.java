package com.example.english.controller;

import com.example.english.dto.request.ContestRequestDTO;
import com.example.english.dto.request.PartResultRequestDTO;
import com.example.english.dto.request.QuestionResponse;
import com.example.english.service.ContestService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contest")
public class ContestController {
  @Autowired private ContestService contestService;
  @PostMapping(value = "")
  public ResponseEntity<?> createExam(@ModelAttribute ContestRequestDTO contestRequestDTO) throws IOException {
    return contestService.createContest(contestRequestDTO);
  }

  @PostMapping(value = "/result")
  public ResponseEntity<?> updateContestResult(@RequestParam(name = "userId") Long userId, @RequestParam(name = "contestId") Long contestId, @RequestBody Float point) {
    return contestService.updateResult(userId, contestId, point);
  }

  @PostMapping(value = "/mark")
  public ResponseEntity<?> markContest(@RequestParam(name = "contestId") Long contestId, @RequestParam(name = "userId") Long userId, @RequestBody
      List<PartResultRequestDTO> partResultRequestDTOS) {
    return contestService.markContest(contestId, userId, partResultRequestDTOS);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getContestById(@RequestParam(name = "contestId") Long contestId) {
    return contestService.getContestById(contestId);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deleteAnswer(@RequestParam(name = "id") Long id) {
    return contestService.deleteContest(id);
  }
}

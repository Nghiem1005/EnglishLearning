package com.example.english.controller;

import com.example.english.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/statistic")
public class StatisticController {
  @Autowired private StatisticService statisticService;

  //Statistic of some basic information of course. Ex: Student amount, Lesson amount, Feedback amount, Like amount
  @GetMapping(value = "/course/info/{courseId}")
  public ResponseEntity<?> statisticInfoCourse(@PathVariable(name = "courseId") Long courseId){
    return statisticService.statisticInfoCourse(courseId);
  }

  //Get best selling course
  @GetMapping(value = "/course/seller")
  public ResponseEntity<?> statisticBestSeller(){
    return statisticService.statisticBestSeller();
  }

  //Statistic user amount, revenue, course amount
  @GetMapping(value = "/general")
  public ResponseEntity<?> generalStatistics(){
    return statisticService.generalStatistics();
  }

  @GetMapping(value = "/day")
  public ResponseEntity<?> statisticsByDay(){
    return statisticService.statisticsByDay();
  }
}

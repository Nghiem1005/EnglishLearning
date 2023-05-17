package com.example.english.exceptions;

import com.example.english.dto.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  private ResponseEntity<ResponseObject> handleResourceAlreadyExists(RuntimeException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  private ResponseEntity<ResponseObject> handleResourceNotFound(RuntimeException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseBody
  protected ResponseEntity<ResponseObject> handleBadRequestExceptions(RuntimeException e){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

}

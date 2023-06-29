package com.example.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
  private HttpStatus status;
  private String message;
  private Object data;
  private int totalPage;
  private int total;

  public ResponseObject(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
    this.data = null;
  }

  public ResponseObject(HttpStatus status, String message, Object data) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.totalPage = 0;
  }

  public ResponseObject(HttpStatus status, String message, Object data, int totalPage) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.totalPage = totalPage;
  }
}

package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class ResultKey implements Serializable {
  private Long practice;
  private Long question;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ResultKey)) {
      return false;
    }
    ResultKey resultKey = (ResultKey) o;
    return Objects.equals(practice, resultKey.practice) && Objects.equals(question,
        resultKey.question);
  }

  @Override
  public int hashCode() {
    return Objects.hash(practice, question);
  }
}

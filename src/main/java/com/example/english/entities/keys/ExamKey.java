package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class ExamKey implements Serializable {
  private Long part;
  private Long lesson;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExamKey)) {
      return false;
    }
    ExamKey examKey = (ExamKey) o;
    return Objects.equals(part, examKey.part) && Objects.equals(lesson,
        examKey.lesson);
  }

  @Override
  public int hashCode() {
    return Objects.hash(part, lesson);
  }
}

package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class LessonExamResultKey implements Serializable {
  private Long lesson;
  private Long practice;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LessonExamResultKey)) {
      return false;
    }
    LessonExamResultKey that = (LessonExamResultKey) o;
    return Objects.equals(lesson, that.lesson) && Objects.equals(practice,
        that.practice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lesson, practice);
  }
}

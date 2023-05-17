package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class LikeCourseKey implements Serializable {
  private Long user;
  private Long course;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LikeCourseKey)) {
      return false;
    }
    LikeCourseKey that = (LikeCourseKey) o;
    return Objects.equals(user, that.user) && Objects.equals(course, that.course);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, course);
  }
}

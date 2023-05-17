package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseKey implements Serializable {
  private Long user;
  private Long course;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StudentCourseKey)) {
      return false;
    }
    StudentCourseKey that = (StudentCourseKey) o;
    return Objects.equals(getUser(), that.getUser()) && Objects.equals(
        getCourse(), that.getCourse());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUser(), getCourse());
  }
}

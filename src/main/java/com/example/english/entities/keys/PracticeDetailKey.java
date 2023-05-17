package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class PracticeDetailKey implements Serializable {
  private Long part;
  private Long practice;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PracticeDetailKey)) {
      return false;
    }
    PracticeDetailKey that = (PracticeDetailKey) o;
    return Objects.equals(part, that.part) && Objects.equals(practice,
        that.practice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(part, practice);
  }
}

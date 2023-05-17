package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class ContestDetailKey implements Serializable {
  private Long part;
  private Long contest;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ContestDetailKey)) {
      return false;
    }
    ContestDetailKey that = (ContestDetailKey) o;
    return Objects.equals(part, that.part) && Objects.equals(contest,
        that.contest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(part, contest);
  }
}

package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class ContestResultKey implements Serializable {
  private Long user;
  private Long contest;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ContestResultKey)) {
      return false;
    }
    ContestResultKey that = (ContestResultKey) o;
    return Objects.equals(user, that.user) && Objects.equals(contest,
        that.contest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, contest);
  }
}

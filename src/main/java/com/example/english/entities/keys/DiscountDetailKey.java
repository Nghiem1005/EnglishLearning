package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class DiscountDetailKey implements Serializable {
  private Long discount;
  private Long course;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DiscountDetailKey)) {
      return false;
    }
    DiscountDetailKey that = (DiscountDetailKey) o;
    return Objects.equals(discount, that.discount) && Objects.equals(course,
        that.course);
  }

  @Override
  public int hashCode() {
    return Objects.hash(discount, course);
  }
}

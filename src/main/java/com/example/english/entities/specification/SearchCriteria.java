package com.example.english.entities.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
  private String key;
  private String operation;
  private Object value;
  private boolean orPredicate;

  public boolean isOrPredicate() {
    return orPredicate;
  }
}

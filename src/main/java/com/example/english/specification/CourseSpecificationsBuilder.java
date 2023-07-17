package com.example.english.specification;

import com.example.english.entities.specification.SearchCriteria;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecificationsBuilder {
  private final List<SearchCriteria> params;

  public CourseSpecificationsBuilder() {
    params = new ArrayList<>();
  }

  public final CourseSpecificationsBuilder with(String key, String operation, Object value) {
    params.add(new SearchCriteria(key, operation, value));
    return this;
  }

  public Specification build() {
    if (params.size() == 0)
      return null;

    Specification result = new CourseSpecification(params.get(0));

    for (int i = 1; i < params.size(); i++) {
      result = params.get(i).isOrPredicate()
          ? Specification.where(result).or(new CourseSpecification(params.get(i)))
          : Specification.where(result).and(new CourseSpecification(params.get(i)));
    }

    return result;
  }
}

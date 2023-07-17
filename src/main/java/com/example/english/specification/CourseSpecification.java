package com.example.english.specification;

import com.example.english.entities.Course;
import com.example.english.entities.specification.SearchCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSpecification implements Specification<Course> {
  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getOperation().equalsIgnoreCase(">")) {
      return criteriaBuilder.greaterThanOrEqualTo(
          root.<String> get(criteria.getKey()), criteria.getValue().toString());
    }
    else if (criteria.getOperation().equalsIgnoreCase("<")) {
      return criteriaBuilder.lessThanOrEqualTo(
          root.<String> get(criteria.getKey()), criteria.getValue().toString());
    }
    else if (criteria.getOperation().equalsIgnoreCase(":")) {
      if (root.get(criteria.getKey()).getJavaType() == String.class) {
        return criteriaBuilder.like(
            root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
      } else {
        return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
      }
    }
    return null;
  }
}

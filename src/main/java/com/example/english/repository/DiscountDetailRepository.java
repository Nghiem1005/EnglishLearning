package com.example.english.repository;

import com.example.english.entities.Course;
import com.example.english.entities.Discount;
import com.example.english.entities.DiscountDetail;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, Long> {
  Optional<DiscountDetail> findDiscountDetailByCourseAndDiscount(Course course, Discount discount);

  @Query(value = "select a from DiscountDetail as a where :c_day > a.discount.startDate and :c_day < a.discount.endDate and a.course.id = :id")
  Optional<DiscountDetail> findDiscountByDayInPeriod(@Param("id") Long id, @Param("c_day") Date c_day);

  List<DiscountDetail> findDiscountDetailsByDiscount(Discount discount);
}

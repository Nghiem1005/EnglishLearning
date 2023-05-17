package com.example.english.repository;

import com.example.english.entities.Course;
import com.example.english.entities.Discount;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
  Optional<Discount> findDiscountByCourse(Course course);

  @Query(value = "select * from tbl_discount where (:c_day > create_date and :c_day < end_date) or (:e_day > create_date and :e_day < end_date)", nativeQuery = true)
  Optional<Discount> findDiscountByDayInPeriod(@Param("c_day") Date c_day, @Param("e_day") Date e_day);


  Optional<Discount> findDiscountByCourseAndCreateDateBeforeAndEndDateAfter(Course course, Date c_Date, Date currentDate);
}

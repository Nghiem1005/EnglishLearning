package com.example.english.repository;

import com.example.english.entities.Course;
import com.example.english.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>,
    JpaSpecificationExecutor<Course> {
  List<Course> findCoursesByTeacher(User teacher);
  @Query(value = "SELECT DISTINCT type FROM tbl_course", nativeQuery = true)
  List<String> getType();
  Page<Course> findCoursesByTeacher(Pageable pageable, User teacher);
  Page<Course> findCoursesByTeacherIsNull(Pageable pageable);

}

package com.example.english.repository;

import com.example.english.entities.User;
import com.example.english.entities.enums.Role;
import com.example.english.models.IStatisticDay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByPhone(String phone);
  Optional<User> findUserByEmail(String email);
  Optional<User> findUserByVerificationCode(String verifyCode);
  List<User> findUsersByRole(Role role);
  @Query(value = "select weekday(create_date) as weekDay, count(id) as totalValue  from tbl_user "
      + "where create_date <= current_date() and create_date > date_sub(current_date(), interval :amountDay Day) "
      + "group by weekday(create_date)", nativeQuery = true)
  List<IStatisticDay> findAllNewMemberByDay(@Param("amountDay") int amountDay);

  @Query(value = "SELECT u.id, u.email, u.enable, u.images, u.name, u.phone, u.role_id, u.provider "
      + "FROM tbl_user as u LEFT JOIN tbl_student_course as sc ON u.id = sc.user_id "
      + "where sc.course_id is null", nativeQuery = true)
  List<User> findUsersOutCourse();
}

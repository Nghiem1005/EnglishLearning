package com.example.english.repository;

import com.example.english.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByPhone(String phone);
  Optional<User> findUserByEmail(String email);
  Optional<User> findUserByVerificationCode(String verifyCode);
}

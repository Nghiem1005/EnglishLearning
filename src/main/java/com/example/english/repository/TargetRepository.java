package com.example.english.repository;

import com.example.english.entities.Target;
import com.example.english.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {
  Optional<Target> findTargetByUser(User user);
}

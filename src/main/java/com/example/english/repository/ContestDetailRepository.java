package com.example.english.repository;

import com.example.english.entities.Contest;
import com.example.english.entities.ContestDetail;
import com.example.english.entities.Part;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestDetailRepository extends JpaRepository<ContestDetail, Long> {
  List<ContestDetail> findContestDetailsByContest(Contest contest);
  Optional<ContestDetail> findContestDetailByContestAndPart(Contest contest, Part part);
}

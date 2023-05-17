package com.example.english.repository;

import com.example.english.entities.Inquiry;
import com.example.english.entities.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
  Page<Inquiry> findInquiriesByLesson(Pageable pageable, Lesson lesson);
  Page<Inquiry> findInquiriesByMainInquiry(Pageable pageable, Inquiry inquiry);
}

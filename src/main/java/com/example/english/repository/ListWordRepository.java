package com.example.english.repository;

import com.example.english.entities.ListWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListWordRepository extends JpaRepository<ListWord, Long> {

}

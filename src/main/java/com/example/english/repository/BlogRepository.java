package com.example.english.repository;

import com.example.english.entities.Blog;
import com.example.english.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findBlogsByUser(User user, Pageable pageable);
}

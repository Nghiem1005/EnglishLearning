package com.example.english.repository;

import com.example.english.entities.Blog;
import com.example.english.entities.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findCommentsByBlog(Pageable pageable, Blog blog);
  List<Comment> findCommentsByBlog(Blog blog);
  Page<Comment> findCommentsByMainComment(Pageable pageable, Comment comment);
  List<Comment> findCommentsByMainComment(Comment comment);

}

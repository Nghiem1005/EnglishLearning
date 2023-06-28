package com.example.english.entities;

import com.example.english.entities.keys.LessonExamResultKey;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_lesson_exam_result")
@IdClass(LessonExamResultKey.class)
public class LessonExamResult {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "lesson_id")
  private Lesson lesson;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "practice_id")
  private Practice practice;
}
